    public void panic() {
        for (MidiChannel ch : synthesizer.getChannels()) {
            ch.allSoundOff();
        }
    }
