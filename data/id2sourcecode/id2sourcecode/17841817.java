    @Override
    public void execute(final Fixture fixture, final ChannelChangeProcessor channelChangeProcessor) {
        Preset preset = fixture.getPreset(presetName);
        if (preset != null) {
            for (int i = 0; i < preset.getValueCount(); i++) {
                PresetValue presetValue = preset.getValue(i);
                FixtureChannel channel = presetValue.getChannel();
                execute(channelChangeProcessor, channel.getNumber(), presetValue.getValue());
            }
        }
    }
