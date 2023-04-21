    @Test
    public void split() {
        ChannelChanges changes = new ChannelChanges();
        changes.add(1, 0);
        changes.add(2, 0);
        changes.add(3, 0);
        changes.add(4, 0);
        ChannelChanges[] cc = changes.split(3);
        assertEquals(3, cc[0].size());
        assertEquals(1, cc[1].size());
        ChannelChange[] cc1 = cc[0].toArray();
        ChannelChange[] cc2 = cc[1].toArray();
        assertEquals(1, cc1[0].getChannelId());
        assertEquals(2, cc1[1].getChannelId());
        assertEquals(3, cc1[2].getChannelId());
        assertEquals(4, cc2[0].getChannelId());
    }
