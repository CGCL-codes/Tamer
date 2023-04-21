    @Override
    public InetAddress getRemoteHost() {
        InetSocketAddress address = (InetSocketAddress) handler.getChannelHandlerContext().getChannel().getRemoteAddress();
        return address.getAddress();
    }
