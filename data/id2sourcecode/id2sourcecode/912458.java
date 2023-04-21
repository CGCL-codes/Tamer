    public void open() throws SoundbankException {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    System.out.println("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 10);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        Soundbank soundbank = synthesizer.getDefaultSoundbank();
        if (soundbank != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        } else {
            throw new SoundbankException();
        }
        MidiChannel midiChannels[] = synthesizer.getChannels();
        channels = new ChannelData[midiChannels.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new ChannelData(midiChannels[i], i);
        }
        currentChannel = channels[0];
    }
