    public void open() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
        instruments = synthesizer.getDefaultSoundbank().getInstruments();
        channels = synthesizer.getChannels();
        String name = instruments[instr].getName();
        if (name.endsWith("\n")) {
            name = name.trim();
        }
        System.out.println("Soundbank instrument " + instr + ": " + name);
        synthesizer.loadInstrument(instruments[instr]);
        channels[channelNum].programChange(instr);
        playChannel(channels[channelNum]);
        System.out.println(n);
    }
