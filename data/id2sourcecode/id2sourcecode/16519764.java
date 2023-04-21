    public synchronized Channel connect(SocketAddress addr) {
        Channel ret = getChannel(addr);
        if (ret == null) {
            ret = new StdChannelImpl(this, addr);
            ioSessions.add(ret);
        }
        return ret;
    }
