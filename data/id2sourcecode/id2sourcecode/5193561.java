        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            ChannelInfo ci = ChannelInfo.newBuilder().setSourceAddress(ByteString.copyFrom(getLocalEndpoint().getAddress().getAddress().getAddress())).setSourcePort(getLocalEndpoint().getAddress().getPort()).build();
            InternetLinkMessage lm = InternetLinkMessage.newBuilder().setExtension(ChannelInfo.extension, ci).build();
            Channel c = ctx.getChannel();
            c.write(lm);
            c.setReadable(false);
            c.getPipeline().remove(ClientChannelHandler.class);
            InternetEndpoint remote_ep = new InternetEndpoint((InetSocketAddress) c.getRemoteAddress());
            InternetLink link = new InternetLink(InternetLinkManager.this, getLocalEndpoint(), remote_ep, c, receive_exec, send_exec);
            connectCompleted(remote_ep, link);
        }
