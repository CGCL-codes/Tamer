    public MidiSound() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Soundbank soundBank = synthesizer.getDefaultSoundbank();
        if (soundBank != null) {
            Instrument instrumentArray[] = soundBank.getInstruments();
            numInstruments = instrumentArray.length;
            System.out.println("Number of Instruments = " + numInstruments);
        }
        channel = synthesizer.getChannels();
        numChannels = channel.length;
        System.out.println("Number of Channels = " + numChannels);
    }
