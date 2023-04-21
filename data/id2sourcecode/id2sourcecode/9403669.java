    private void assertChannels(final String attributeName, final int... channelNumbers) {
        Attribute attribute = fixture.getAttribute(attributeName);
        assertEquals(attribute.getChannelCount(), channelNumbers.length);
        for (int i = 0; i < channelNumbers.length; i++) {
            assertEquals(attribute.getChannel(i).getNumber(), channelNumbers[i]);
        }
    }
