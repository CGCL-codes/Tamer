    @Override
    public void close() {
        this.closed = true;
        rl.readLock().lock();
        ArrayList<NettyClientServerSession> sessions = new ArrayList<NettyClientServerSession>(sessionTable.values());
        rl.readLock().unlock();
        for (NettyClientServerSession ncss : sessions) {
            ncss.getChannel().close();
        }
    }
