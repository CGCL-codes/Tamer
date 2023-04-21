        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            InternetLinkMessage msg = (InternetLinkMessage) e.getMessage();
            if (!msg.hasExtension(ChannelInfo.extension)) throw new IllegalStateException();
            ChannelInfo ci = msg.getExtension(ChannelInfo.extension);
            InternetEndpoint remote_ep = null;
            if (ci.hasSourceAddress() && ci.hasSourcePort()) {
                byte[] remote_addr_bytes = ci.getSourceAddress().toByteArray();
                InetAddress remote_addr = InetAddress.getByAddress(remote_addr_bytes);
                int remote_port = ci.getSourcePort();
                InetSocketAddress remote_saddr = new InetSocketAddress(remote_addr, remote_port);
                remote_ep = new InternetEndpoint(remote_saddr);
            }
            Channel c = ctx.getChannel();
            c.setReadable(false);
            c.getPipeline().remove(ServerChannelHandler.class);
            InternetLink link = new InternetLink(InternetController.this, bind, remote_ep, c);
            feedUpstream(new NetworkAcceptEvent<InternetEndpoint>(bind, remote_ep, link));
        }
