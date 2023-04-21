    public void testMoveChannelBetweenGroups() throws Exception {
        PersistChanGrpMgr sourceGrp, destGrp;
        sourceGrp = makeEmptyGroup("Source Group");
        destGrp = makeEmptyGroup("Destination Group");
        int count = 5;
        int sourceHas = count;
        int destHas = 0;
        addGenChannels(sourceGrp, count, 3);
        for (int i = 0; i < count; i++) {
            assertValidGroup(sourceGrp, "Source Group", sourceHas - i);
            assertValidGroup(destGrp, "Destination Group", destHas + i);
            Channel channelToMove;
            Collection chans = sourceGrp.getChannelGroup().getChannels();
            Iterator iter = chans.iterator();
            assertTrue(chans.size() > 0);
            channelToMove = (Channel) iter.next();
            sourceGrp.moveChannelTo(channelToMove, destGrp);
        }
    }
