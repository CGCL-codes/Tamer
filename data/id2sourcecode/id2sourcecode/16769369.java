    @Test
    public void testColorCommand() {
        buildFixtures();
        FixtureControl control = new FixtureControl(getContext(), channelChangeProcessor);
        control.process(new SetColor(new Color(23, 127, 255)));
        assertEquals(channelChangeProcessor.getChannelChangeCount(), 6);
        assertChannelChange(0, 13, 23);
        assertChannelChange(1, 14, 127);
        assertChannelChange(2, 15, 255);
        assertChannelChange(3, 23, 255 - 23);
        assertChannelChange(4, 24, 255 - 127);
        assertChannelChange(5, 25, 255 - 255);
    }
