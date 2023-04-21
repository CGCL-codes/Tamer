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
                    if (oldReader instanceof InterestReadChannel && oldReader != newReader) {
                        if (lastChannel instanceof ThrottleListener) ((ThrottleListener) lastChannel).setAttachment(AbstractNBSocket.this);
                        lastChannel.setReadChannel((InterestReadChannel) oldReader);
                        reader.handleRead();
                        oldReader.shutdown();
                    }
                    InterestReadChannel source = getBaseReadChannel();
                    lastChannel.setReadChannel(source);
                    NIODispatcher.instance().interestRead(getChannel(), true);
                } catch (IOException iox) {
                    shutdown();
                    oldReader.shutdown();
                }
            }
