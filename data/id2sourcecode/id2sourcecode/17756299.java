    @Override
    public ServerJPIPChannel getChannel(String cid) {
        assert (cid != null);
        if (!contains(cid)) return null;
        return (ServerJPIPChannel) channels.get(cid);
    }
