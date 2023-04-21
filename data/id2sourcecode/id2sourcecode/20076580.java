    public CastaneaPlayer(ClientSession session) {
        this.name = session.getName();
        this.sessionRef = AppContext.getDataManager().createReference(session);
        AppContext.getDataManager().setBinding(name, this);
        AppContext.getChannelManager().getChannel(CastaneaServer.WORLD).join(session);
    }
