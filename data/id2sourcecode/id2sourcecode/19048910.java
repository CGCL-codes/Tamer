                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            future.getChannel().write(buf);
                        } else {
                            logger.error("Remote connection closed.");
                            closeLocalChannel();
                        }
                    }
