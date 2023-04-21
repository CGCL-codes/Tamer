    @Test
    public void constructor() {
        ChannelChange cc = new ChannelChange(10, 100);
        assertEquals(cc.getChannelId(), 10);
        assertEquals(cc.getDmxValue(), 100);
    }
