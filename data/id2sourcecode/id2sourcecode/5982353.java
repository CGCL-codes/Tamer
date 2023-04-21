    @Override
    public int getRemotePort() {
        InetSocketAddress address = (InetSocketAddress) handler.getChannelHandlerContext().getChannel().getRemoteAddress();
        return address.getPort();
    }
