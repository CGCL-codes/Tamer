    public SocketChannel requestTCPChannel(IPeer to, ConnectionRequestType type, InetSocketAddress bindTo) {
        Iterator<IEstablishChanMechanism<SocketChannel>> it = mechanismManager.getTCPMechanisms();
        while (it.hasNext()) {
            IEstablishChanMechanism<SocketChannel> mech = it.next();
            mech.setBindAddress(bindTo);
            IConnectionInfo<SocketChannel> info = mech.establishConn(to, type);
            SocketChannel sc = (info != null ? info.getChannel() : null);
            if (sc != null && sc.isConnected()) {
                if (!sc.socket().getRemoteSocketAddress().equals(to.getAddress())) onEstablishedRelay(sc, (InetSocketAddress) sc.socket().getRemoteSocketAddress(), to.getAddress());
                return sc;
            } else closeWithoutException(sc);
        }
        return null;
    }
