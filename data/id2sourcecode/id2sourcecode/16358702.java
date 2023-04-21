    public static void closeNote(int pitch, int channel) {
        initSynthesizer();
        MidiChannel voiceChannel = synthesizer.getChannels()[channel];
        voiceChannel.noteOff(pitch);
        channels[channel][pitch] = 0;
    }
