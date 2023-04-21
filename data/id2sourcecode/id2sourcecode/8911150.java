    private void initSound() {
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
        Soundbank sb = SonicRampPicker.sb;
        if (sb == null) {
            SonicRampPicker.openSoundbank();
        }
        if (sb != null) {
            instruments = sb.getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        MidiChannel[] midiChannels = synthesizer.getChannels();
        cc = new ChannelData(midiChannels[0], 0);
    }
