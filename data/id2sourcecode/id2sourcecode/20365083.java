                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    if (event.getChannel().getPipeline().get("ssl") == null) {
                                        InetSocketAddress remote = (InetSocketAddress) event.getChannel().getRemoteAddress();
                                        SSLEngine engine = sslContext.createSSLEngine(remote.getAddress().getHostAddress(), remote.getPort());
                                        engine.setUseClientMode(false);
                                        event.getChannel().getPipeline().addBefore("decoder", "ssl", new SslHandler(engine));
                                    }
                                }
