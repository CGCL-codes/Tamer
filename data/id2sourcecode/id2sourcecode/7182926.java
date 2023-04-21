    public void testChannelMembership() throws Exception {
        final PersistChanGrpMgr agroup;
        agroup = makeEmptyGroup("Membership Group");
        addGenChannels(agroup, 3, 3);
        final Iterator channels = agroup.getChannelGroup().getChannels().iterator();
        while (channels.hasNext()) {
            final Channel nextchan = (Channel) channels.next();
            assertTrue("Expected member channel not found", agroup.hasChannel(nextchan));
        }
    }
