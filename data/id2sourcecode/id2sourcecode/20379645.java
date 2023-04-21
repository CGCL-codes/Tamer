                            public void operationComplete(ChannelFuture arg0) throws Exception {
                                closeChannelPair(arg0.getChannel());
                            }
