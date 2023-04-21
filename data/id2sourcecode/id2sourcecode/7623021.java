    public void assertChannelChange(final int index, final int channelId, final int dmxValue) {
        ChannelChange change = get(index);
        assertEquals(change.getChannelId(), channelId);
        assertEquals(change.getDmxValue(), dmxValue);
    }
