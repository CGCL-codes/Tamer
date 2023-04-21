    protected void assertValidGroup(PersistChanGrpMgr gp, String name, int size) throws Exception {
        ChannelGroup gpc = gp.getChannelGroup();
        assertEquals(gpc.getTitle(), name);
        assertEquals(size, gpc.getChannels().size());
    }
