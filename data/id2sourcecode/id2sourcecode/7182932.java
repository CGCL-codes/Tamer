    public void testItemCount() throws Exception {
        PersistChanGrpMgr countedGrp;
        countedGrp = makeEmptyGroup("Item Count Group");
        int count = 10;
        addGenChannels(countedGrp, count, 5);
        Object[] channels = countedGrp.getChannelGroup().getChannels().toArray();
        assertEquals("Wrong number of channels in group", channels.length, count);
        for (int i = 0; i < count; i++) {
            Channel aChan = (Channel) channels[i];
            Object[] items = aChan.getItems().toArray();
            int howManyItems = items.length;
            assertEquals("Wrong Count returned", howManyItems, countedGrp.getItemCount(aChan));
        }
    }
