    public static int openNote(int instrument, int pitch, int velocity) {
        initSynthesizer();
        int c = 0;
        for (int i = 0; i < 8; i++) {
            if (channels[i][pitch] == 0) {
                channels[i][pitch] = 1;
                c = i;
                break;
            }
        }
        MidiChannel voiceChannel = synthesizer.getChannels()[c];
        synthesizer.loadInstrument(instruments[instrument]);
        voiceChannel.programChange(instrument);
        voiceChannel.noteOn(pitch, velocity);
        return c;
    }
