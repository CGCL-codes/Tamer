    public ServerJPIPChannel getJPIPChannel(String cid) {
        if (cid == null) throw new NullPointerException();
        ServerClientSessionTarget sessionTarget = (ServerClientSessionTarget) getSessionTarget(cid);
        if (sessionTarget != null) return sessionTarget.getChannel(cid);
        return null;
    }
