    @Override
    protected void handleDownstream(NetworkControlMessage<InternetEndpoint> message) {
        if (message instanceof NetworkBindRequest<?>) {
            NetworkBindRequest<InternetEndpoint> nbrq = (NetworkBindRequest<InternetEndpoint>) message;
            NetworkBindResponse<InternetEndpoint> nbrp;
            if (bind == null) {
                nbrp = new NetworkBindResponse<InternetEndpoint>(nbrq, new IllegalStateException("This InternetController is already bound to " + bind + "."));
            } else {
                try {
                    setupServer(nbrq.getEndpoint());
                    bind = nbrq.getEndpoint();
                    nbrp = new NetworkBindResponse<InternetEndpoint>(nbrq);
                } catch (Throwable t) {
                    nbrp = new NetworkBindResponse<InternetEndpoint>(nbrq, t);
                }
            }
            feedUpstream(nbrp);
        } else if (message instanceof NetworkLinkRequest<?>) {
            final NetworkLinkRequest<InternetEndpoint> nlrq = (NetworkLinkRequest<InternetEndpoint>) message;
            try {
                setupClient(nlrq.getEndpoint()).addListener(new ChannelFutureListener() {

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
                });
            } catch (Throwable t) {
                feedUpstream(new NetworkLinkResponse<InternetEndpoint>(nlrq, t));
            }
        }
    }
