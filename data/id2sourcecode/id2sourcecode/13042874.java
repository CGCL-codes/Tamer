    private PersistChanGrpMgr getPersistChanGrpMgr() {
        PersistentInformaChannelGuide chanGuide = (PersistentInformaChannelGuide) GlobalModel.SINGLETON.getChannelGuideSet().mapCGE2ChannelGuide(this);
        return chanGuide.getPersistChanGrpMgr();
    }
