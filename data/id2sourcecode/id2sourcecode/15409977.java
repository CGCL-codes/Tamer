    private void initSynth() {
        if (synthesizer != null) return;
        utils.log("Creation (unique) du synthetiser");
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            Instrument[] instruments = synthesizer.getDefaultSoundbank().getInstruments();
            midiChannels = synthesizer.getChannels();
            Instrument instrument = instruments[instrumentIndex];
            synthesizer.loadInstrument(instrument);
            midiChannels[0].programChange(instrumentIndex);
        } catch (Exception e) {
            return;
        }
    }
