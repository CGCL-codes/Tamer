            public void run() {
                ReadObserver oldReader = reader;
                try {
                    synchronized (LOCK) {
                        if (shutdown) {
                            newReader.shutdown();
                            return;
                        }
                        reader = newReader;
                    }
                    ChannelReader lastChannel = newReader;
                    while (lastChannel.getReadChannel() instanceof ChannelReader) lastChannel = (ChannelReader) lastChannel.getReadChannel();
                    if (lastChannel instanceof RequiresSelectionKeyAttachment) ((RequiresSelectionKeyAttachment) lastChannel).setAttachment(AbstractNBSocket.this);
                    if (oldReader instanceof InterestReadableByteChannel && oldReader != newReader) {
                        lastChannel.setReadChannel((InterestReadableByteChannel) oldReader);
                        reader.handleRead();
                        oldReader.shutdown();
                    }
                    InterestReadableByteChannel source = getBaseReadChannel();
                    lastChannel.setReadChannel(source);
                    source.interestRead(true);
                    if (isConnected() && !NIODispatcher.instance().isReadReadyThisIteration(getChannel())) reader.handleRead();
                } catch (IOException iox) {
                    shutdown();
                    oldReader.shutdown();
                }
            }
