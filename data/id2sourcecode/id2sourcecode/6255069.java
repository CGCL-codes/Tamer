    public void testRemoveChannelsFromGroup() throws Exception {
        PersistChanGrpMgr theGrp;
        int count = 5;
        theGrp = makeEmptyGroup("The Group");
        addGenChannels(theGrp, count, 5);
        for (int i = 0; i < count; i++) {
            ChannelBuilder bld = theGrp.getBuilder();
            bld.beginTransaction();
            bld.update(theGrp.getChannelGroup());
            assertValidGroup(theGrp, "The Group", count - i);
            Channel channelToDelete;
            Collection chans = theGrp.getChannelGroup().getChannels();
            Iterator iter = chans.iterator();
            assertTrue(chans.size() > 0);
            channelToDelete = (Channel) iter.next();
            bld.endTransaction();
            theGrp.deleteChannel(channelToDelete);
        }
    }
