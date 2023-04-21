    @SuppressWarnings("unused")
    private void initSound_old() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    logger.severe("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        Soundbank sb = synthesizer.getDefaultSoundbank();
        if (sb != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        MidiChannel[] midiChannels = synthesizer.getChannels();
        cc = new ChannelData(midiChannels[0], 0);
        setInstrument(27);
    }
