    @Test
    public void get() {
        ChannelChangeQueue queue = new ChannelChangeQueue();
        assertEquals(queue.get().size(), 0);
        queue.add(new ChannelChange(1, 0));
        queue.add(new ChannelChange(2, 0));
        ChannelChange[] changes = queue.get().toArray();
        assertEquals(changes.length, 2);
        assertEquals(changes[0].getChannelId(), 1);
        assertEquals(changes[1].getChannelId(), 2);
        assertEquals(queue.size(), 0);
        assertEquals(queue.get().size(), 0);
    }
