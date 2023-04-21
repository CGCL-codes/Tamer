    protected Format[] getMatchingOutputFormats(Format in) {
        AudioFormat inFormat = (AudioFormat) in;
        int channels = inFormat.getChannels();
        int sampleRate = (int) (inFormat.getSampleRate());
        if (channels == 2) {
            supportedOutputFormats = new AudioFormat[] { new AudioFormat(AudioFormat.ALAW, sampleRate, 8, 2, Format.NOT_SPECIFIED, Format.NOT_SPECIFIED), new AudioFormat(AudioFormat.ALAW, sampleRate, 8, 1, Format.NOT_SPECIFIED, Format.NOT_SPECIFIED) };
        } else {
            supportedOutputFormats = new AudioFormat[] { new AudioFormat(AudioFormat.ALAW, sampleRate, 8, 1, Format.NOT_SPECIFIED, Format.NOT_SPECIFIED) };
        }
        return supportedOutputFormats;
    }
