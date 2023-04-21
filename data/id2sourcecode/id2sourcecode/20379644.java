                    public void operationComplete(ChannelFuture arg0) throws Exception {
                        Channel channel = arg0.getChannel();
                        HttpChannelAssociations.channels.put(e.getChannel(), channel);
                        HttpChannelAssociations.channels.put(channel, e.getChannel());
                        if (request.isChunked()) {
                            readingChunks = true;
                        }
                        channel.write(request);
                        channel.getCloseFuture().addListener(new ChannelFutureListener() {

                            public void operationComplete(ChannelFuture arg0) throws Exception {
                                closeChannelPair(arg0.getChannel());
                            }
                        });
                    }
