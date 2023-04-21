                            @Override
                            public void callback(Channel remote) throws Exception {
                                remoteChannel = remote;
                                if (null == remoteChannel) {
                                    close();
                                    return;
                                }
                                initCodecHandler(remoteChannel);
                                if (!isHttps || needForwardConnect()) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Send proxy request");
                                        logger.debug(request.toString());
                                    }
                                    remoteChannel.write(request);
                                } else if (isHttps) {
                                    HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                    ChannelFuture future = event.getChannel().write(res);
                                    removeCodecHandler(future);
                                }
                            }
