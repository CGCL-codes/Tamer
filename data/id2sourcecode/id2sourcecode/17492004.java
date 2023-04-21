                            public void operationComplete(final ChannelFuture future) throws Exception {
                                remoteFuture.getChannel().write(buf);
                            }
