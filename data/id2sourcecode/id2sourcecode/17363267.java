    public Format[] getSupportedOutputFormats(Format in) {
        if (in == null) {
            return new Format[] { new AudioFormat(AudioFormat.ALAW) };
        }
        if (matches(in, inputFormats) == null) {
            return new Format[1];
        }
        if (!(in instanceof AudioFormat)) {
            return new Format[] { new AudioFormat(AudioFormat.ALAW) };
        }
        AudioFormat af = (AudioFormat) in;
        return new Format[] { new AudioFormat(AudioFormat.ALAW, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels()) };
    }
