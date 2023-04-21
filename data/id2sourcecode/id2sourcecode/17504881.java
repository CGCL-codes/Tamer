    private void assertDimmerChannel(final String expected, final int index) {
        Dimmer dimmer = newShow.getDimmers().get(index);
        Channel channel = dimmer.getChannel();
        String name = "";
        if (channel != null) {
            name = channel.getName();
        }
        assertEquals(name, expected);
    }
