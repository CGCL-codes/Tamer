    public MidiEvent[] getMidiEvents(MidiContext context) {
        Debug.println(this);
        context.setDrum(getChannel(), isDrum() ? MidiContext.ChannelConfiguration.PERCUSSION : MidiContext.ChannelConfiguration.SOUND_SET);
        return null;
    }
