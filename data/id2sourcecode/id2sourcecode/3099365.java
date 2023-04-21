    private void makeChannel(MidiSequenceHandler sequence, TGChannel channel, int track, boolean primary) {
        int number = (primary ? channel.getChannel() : channel.getEffectChannel());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.VOLUME, channel.getVolume());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.BALANCE, channel.getBalance());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.CHORUS, channel.getChorus());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.REVERB, channel.getReverb());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.PHASER, channel.getPhaser());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.TREMOLO, channel.getTremolo());
        sequence.addControlChange(getTick(TGDuration.QUARTER_TIME), track, number, MidiControllers.EXPRESSION, 127);
        sequence.addProgramChange(getTick(TGDuration.QUARTER_TIME), track, number, channel.getInstrument());
    }
