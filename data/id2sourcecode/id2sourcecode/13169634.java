    public MidiSoundCard(Memory mem, int memBegin) throws MidiUnavailableException {
        super(mem, memBegin);
        this.synthesizer = javax.sound.midi.MidiSystem.getSynthesizer();
        this.synthesizer.open();
        this.channels = synthesizer.getChannels();
    }
