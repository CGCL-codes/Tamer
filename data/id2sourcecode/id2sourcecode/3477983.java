    private void writeNextAndWait(final Channel channel) {
        final long elapsedTime = System.currentTimeMillis() - startTime;
        final long elapsedTimePlusSeek = elapsedTime + seekTime;
        final double clientBuffer = timePosition - elapsedTimePlusSeek;
        if (logger.isDebugEnabled()) {
            logger.debug("bytes written, actual: {}, last client ack: {}, window: {}", new Object[] { bytesWritten, bytesWrittenLastReceived, bytesWrittenWindow });
            logger.debug("elapsed: {}, streamed: {}, buffer: {}", new Object[] { elapsedTimePlusSeek, timePosition, clientBuffer });
        }
        if (clientBuffer > 100) {
            reader.setAggregateDuration((int) clientBuffer);
        } else {
            reader.setAggregateDuration(0);
        }
        if (!reader.hasNext() || playLength >= 0 && timePosition > (seekTime + playLength)) {
            playStopSequence(channel);
            return;
        }
        final RtmpMessage message = reader.next();
        final RtmpHeader header = message.getHeader();
        final double compensationFactor = clientBuffer / TARGET_BUFFER_DURATION;
        final long delay = (long) ((header.getTime() - timePosition) * compensationFactor);
        timePosition = header.getTime();
        header.setStreamId(streamId);
        final WriteNext writeNext = new WriteNext(currentConversationId);
        final long writeTime = System.currentTimeMillis();
        final ChannelFuture future = channel.write(message);
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture cf) {
                final long completedIn = System.currentTimeMillis() - writeTime;
                if (completedIn > 1000) {
                    logger.warn("channel busy? time taken to write last message: {}", completedIn);
                }
                final long delayToUse = delay - completedIn;
                if (delayToUse > RtmpConfig.TIMER_TICK_SIZE) {
                    timer.newTimeout(new TimerTask() {

                        @Override
                        public void run(Timeout timeout) {
                            Channels.fireMessageReceived(future.getChannel(), writeNext);
                        }
                    }, delayToUse, TimeUnit.MILLISECONDS);
                } else {
                    Channels.fireMessageReceived(future.getChannel(), writeNext);
                }
            }
        });
    }
