    public PersistChanGrpMgr makeEmptyGroup(String name) {
        PersistChanGrpMgr res;
        logger.info("Creating group: " + name);
        res = new PersistChanGrpMgr(handler, false);
        res.createGroup(name);
        logger.info("Result: " + res);
        assertEquals("Newly created group has non-zero Channels", 0, res.getChannelGroup().getChannels().size());
        return res;
    }
