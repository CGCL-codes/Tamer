    protected Format[] getMatchingOutputFormats(Format in) {
        AudioFormat af = (AudioFormat) in;
        supportedOutputFormats = new AudioFormat[] { new AudioFormat(AudioFormat.LINEAR, af.getSampleRate(), 16, af.getChannels(), AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED) };
        return supportedOutputFormats;
    }
