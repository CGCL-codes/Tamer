                    @Override
                    public void operationComplete(ChannelFuture arg0) throws Exception {
                        if (!arg0.isSuccess()) {
                            feedUpstream(new NetworkLinkResponse<InternetEndpoint>(nlrq, arg0.getCause()));
                        } else {
                            Channel c = arg0.getChannel();
                            ChannelInfo.Builder cib = ChannelInfo.newBuilder();
                            if (bind != null) {
                                cib.setSourceAddress(ByteString.copyFrom(bind.getAddress().getAddress().getAddress()));
                                cib.setSourcePort(bind.getAddress().getPort());
                            }
                            InternetLinkMessage lm = InternetLinkMessage.newBuilder().setExtension(ChannelInfo.extension, cib.build()).build();
                            c.write(lm);
                            c.setReadable(false);
                            c.getPipeline().remove(ClientChannelHandler.class);
                            InternetEndpoint remote_ep = new InternetEndpoint((InetSocketAddress) c.getRemoteAddress());
                            InternetLink link = new InternetLink(InternetController.this, bind, remote_ep, c);
                            feedUpstream(new NetworkLinkResponse<InternetEndpoint>(nlrq, bind, remote_ep, link));
                        }
                    }
