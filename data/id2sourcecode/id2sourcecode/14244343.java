                    @Override
                    public void operationComplete(ChannelFuture future) {
                        try {
                            PMS.get().getRegistry().reenableGoToSleep();
                            inputStream.close();
                        } catch (IOException e) {
                        }
                        future.getChannel().close();
                        startStopListenerDelegate.stop();
                    }
