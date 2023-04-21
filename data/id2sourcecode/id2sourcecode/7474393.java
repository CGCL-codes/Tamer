    private void initPresetValue(final Preset preset, final PresetValueDefinition presetValueDefinition, final Attribute attribute) {
        int[] values = presetValueDefinition.getValues(attribute.getChannelCount());
        for (int i = 0; i < values.length; i++) {
            FixtureChannel channel = attribute.getChannel(i);
            PresetValue presetValue = new PresetValue(channel, values[i]);
            preset.addValue(presetValue);
        }
    }
