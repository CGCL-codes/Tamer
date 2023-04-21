    @Test
    public void testSettingAttributesForSelectedFixtures() {
        buildFixtures();
        FixtureControl control = new FixtureControl(getContext(), channelChangeProcessor);
        fixture1.setSelected(true);
        fixture2.setSelected(true);
        control.process(new SetAttribute(Attribute.INTENSITY, 255));
        assertEquals(channelChangeProcessor.getChannelChangeCount(), 2);
        assertChannelChange(0, 10, 255);
        assertChannelChange(1, 20, 255);
        fixture2.setSelected(false);
        fixture3.setSelected(true);
        control.process(new SetAttribute(Attribute.INTENSITY, 127));
        assertEquals(channelChangeProcessor.getChannelChangeCount(), 4);
        assertChannelChange(2, 10, 127);
        assertChannelChange(3, 30, 127);
    }
