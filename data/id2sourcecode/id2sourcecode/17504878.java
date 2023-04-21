    private void assertChannelName(final String expected, final int index) {
        String name = newShow.getChannels().get(index).getName();
        assertEquals(name, expected);
    }
