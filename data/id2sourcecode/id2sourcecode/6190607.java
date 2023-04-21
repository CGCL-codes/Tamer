    public PersistChanGrpMgrTask(PersistChanGrpMgr mgr, long minChannelUpdateDelay) {
        super("PCGrp: " + mgr.getChannelGroup().getTitle());
        this.minChannelUpdateDelay = minChannelUpdateDelay;
        this.mgr = mgr;
        builder = mgr.getBuilder();
        channelInfos = new HashMap<URL, UpdateChannelInfo>();
        tempBuilder = new de.nava.informa.impl.basic.ChannelBuilder();
    }
