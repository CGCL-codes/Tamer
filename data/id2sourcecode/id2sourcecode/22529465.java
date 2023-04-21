    private MidiController() throws MidiSubsystemException {
        try {
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
        } catch (MidiUnavailableException e) {
            throw new MidiSubsystemException(e.getMessage());
        }
        MidiChannel[] channels = this.synth.getChannels();
        this.channel = null;
        for (int i = 0; i < channels.length && this.channel == null; i++) {
            if (channels[i] != null) {
                this.channel = channels[i];
            }
        }
        if (channel == null) {
            throw new MidiSubsystemException(ERROR_NOCHANNEL);
        }
        this.instrument = this.getInstrumentIndex(0);
        this.setInstrument(instrument);
        this.setVelocity(INITIAL_VELOCITY);
    }
