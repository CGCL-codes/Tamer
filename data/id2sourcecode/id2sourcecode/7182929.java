    public void testDeleteItemsFromChannels() throws Exception {
        PersistChanGrpMgr delGrp;
        delGrp = makeEmptyGroup("deleteTest Group");
        int count = 10;
        addGenChannels(delGrp, count, 5);
        Object[] channels = delGrp.getChannelGroup().getChannels().toArray();
        assertEquals("Wrong number of channels in group", channels.length, count);
        for (int i = 0; i < count; i++) {
            Channel aChan = (Channel) channels[i];
            Object[] items = aChan.getItems().toArray();
            int howManyItems = items.length;
            for (int j = 0; j < howManyItems; j++) {
                Item anItem = (Item) items[j];
                assertEquals("Wrong number of items after delete", howManyItems - j, aChan.getItems().size());
                delGrp.deleteItemFromChannel(aChan, anItem);
            }
        }
    }
