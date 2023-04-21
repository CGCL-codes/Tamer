                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LOG.debug("request complete isSuccess : {}", future.isSuccess());
                    if (future.isSuccess() == false) {
                        if (0 < forwarders.size()) {
                            sendRequest(e, original, bootstrap, forwarders);
                        } else {
                            original.header().rcode(RCode.ServFail);
                            ChannelBuffer buffer = ChannelBuffers.buffer(512);
                            original.write(buffer);
                            e.getChannel().write(buffer).addListener(ChannelFutureListener.CLOSE);
                        }
                    }
                }
