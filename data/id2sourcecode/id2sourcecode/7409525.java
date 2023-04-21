    @Test
    public void split() {
        ChannelChanges changes = new ChannelChanges();
        changes.add(1, 0);
        changes.add(2, 0);
        changes.add(3, 0);
        changes.add(4, 0);
        ChannelChanges[] cc = changes.split(3);
        assertEquals(cc[0].size(), 3);
        assertEquals(cc[1].size(), 1);
        ChannelChange[] cc1 = cc[0].toArray();
        ChannelChange[] cc2 = cc[1].toArray();
        assertEquals(cc1[0].getChannelId(), 1);
        assertEquals(cc1[1].getChannelId(), 2);
        assertEquals(cc1[2].getChannelId(), 3);
        assertEquals(cc2[0].getChannelId(), 4);
    }
