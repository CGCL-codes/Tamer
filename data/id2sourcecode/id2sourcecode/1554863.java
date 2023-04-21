    private DatagramChannel requestUDPChannel(IPeer to, ConnectionRequestType type, InetSocketAddress bindTo, boolean executeAnswerTest) {
        Iterator<IEstablishChanMechanism<DatagramChannel>> it = mechanismManager.getUDPMechanisms();
        while (it.hasNext()) {
            IEstablishChanMechanism<DatagramChannel> mech = it.next();
            mech.setBindAddress(bindTo);
            IConnectionInfo<DatagramChannel> info = mech.establishConn(to, type);
            if (info == null) continue;
            DatagramChannel dc = info.getChannel();
            if (!dc.isConnected()) {
                closeWithoutException(dc);
            }
            try {
                if (dc.isBlocking()) dc.configureBlocking(false);
            } catch (IOException e) {
            }
            boolean isEstablished = true;
            if (executeAnswerTest) isEstablished = getCallback().testUDPConnectivity(dc);
            if (isEstablished) {
                if (!dc.socket().getRemoteSocketAddress().equals(to.getAddress())) onEstablishedRelay(dc, (InetSocketAddress) dc.socket().getRemoteSocketAddress(), to.getAddress());
                return dc;
            } else {
                closeWithoutException(dc);
            }
        }
        return null;
    }
