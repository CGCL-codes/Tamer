    private void assertPreset(final String presetName, final int valueIndex, final String expectedChannelName, final int expectedValue) {
        Preset preset = fixture.getPreset(presetName);
        PresetValue presetValue = preset.getValue(valueIndex);
        assertEquals(presetValue.getChannel().getName(), expectedChannelName);
        assertEquals(presetValue.getValue(), expectedValue);
    }
