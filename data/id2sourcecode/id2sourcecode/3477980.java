    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) {
        final Channel channel = e.getChannel();
        if (e.getMessage() instanceof WriteNext) {
            final WriteNext mw = (WriteNext) e.getMessage();
            if (mw.conversationId != currentConversationId) {
                logger.info("stopping obsolete conversation id: {}", mw.conversationId);
                return;
            }
            writeNextAndWait(channel);
            return;
        }
        final RtmpMessage message = (RtmpMessage) e.getMessage();
        bytesRead += message.getHeader().getSize();
        if ((bytesRead - bytesReadLastSent) > bytesReadWindow) {
            logger.info("sending bytes read ack after: {}", bytesRead);
            BytesRead ack = new BytesRead(bytesRead);
            channel.write(ack);
            bytesReadLastSent = bytesRead;
        }
        switch(message.getHeader().getMessageType()) {
            case COMMAND_AMF0:
                final Command command = (Command) message;
                final String name = command.getName();
                if (name.equals("connect")) {
                    connectResponse(channel, command);
                } else if (name.equals("createStream")) {
                    streamId = 1;
                    channel.write(Command.createStreamSuccess(command.getTransactionId(), streamId));
                } else if (name.equals("play")) {
                    playResponse(channel, command);
                } else if (name.equals("deleteStream")) {
                    int deleteStreamId = ((Double) command.getArg(0)).intValue();
                    logger.info("deleting stream id: {}", deleteStreamId);
                    ChannelFuture future = channel.write(Control.streamEof(deleteStreamId));
                    future.addListener(ChannelFutureListener.CLOSE);
                } else if (name.equals("closeStream")) {
                    final int clientStreamId = command.getHeader().getStreamId();
                    logger.info("closing stream id: {}", clientStreamId);
                    streamId = 0;
                } else if (name.equals("pause")) {
                    pauseResponse(channel, command);
                } else if (name.equals("seek")) {
                    seekResponse(channel, command);
                } else {
                    logger.warn("ignoring client command: {}", command);
                }
                break;
            case BYTES_READ:
                final BytesRead bytesReadByClient = (BytesRead) message;
                bytesWrittenLastReceived = bytesReadByClient.getValue();
                logger.info("client bytes read: {}, actual: {}", bytesReadByClient, bytesWritten);
                break;
            case WINDOW_ACK_SIZE:
                WindowAckSize was = (WindowAckSize) message;
                if (was.getValue() != bytesReadWindow) {
                    channel.write(SetPeerBw.dynamic(bytesReadWindow));
                }
                break;
            case SET_PEER_BW:
                SetPeerBw spb = (SetPeerBw) message;
                if (spb.getValue() != bytesWrittenWindow) {
                    channel.write(new WindowAckSize(bytesWrittenWindow));
                }
                break;
            default:
                logger.warn("ignoring rtmp message: {}", message);
        }
    }
