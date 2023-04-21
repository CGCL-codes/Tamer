    public Channel getChannel(SocketAddress addr) {
        synchronized (ioSessions) {
            for (Iterator itor = ioSessions.iterator(); itor.hasNext(); ) {
                StdChannelImpl cur = (StdChannelImpl) itor.next();
                if (cur.getSocketAddress().equals(addr)) return cur;
            }
            return null;
        }
    }
