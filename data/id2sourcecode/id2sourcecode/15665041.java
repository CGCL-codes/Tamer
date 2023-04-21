    public TCPChannel getChannel(Endpoint ep) {
        TCPChannel ch = null;
        if (ep instanceof TCPEndpoint) {
            synchronized (channelTable) {
                Reference<TCPChannel> ref = channelTable.get(ep);
                if (ref != null) {
                    ch = ref.get();
                }
                if (ch == null) {
                    TCPEndpoint tcpEndpoint = (TCPEndpoint) ep;
                    ch = new TCPChannel(this, tcpEndpoint);
                    channelTable.put(tcpEndpoint, new WeakReference<TCPChannel>(ch));
                }
            }
        }
        return ch;
    }
