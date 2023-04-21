    private void assertChannelChange(final int index, final int channelId, final int dmxValue) {
        ChannelChange change = channelChangeProcessor.get(index);
        assertEquals(change.getChannelId(), channelId);
        assertEquals(change.getDmxValue(), dmxValue);
    }
