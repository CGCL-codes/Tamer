    public AbstractChannelName getChannelName(IContextFactory currentCtx) {
        SocketContextServer sock = getSocketContextServer(this, getAppThreadPool(), contextName);
        return sock.isAvailable() ? new SocketChannelName(sock.getChannelName(), currentCtx) : null;
    }
