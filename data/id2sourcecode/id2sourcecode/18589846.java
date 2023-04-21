    protected Format[] getMatchingOutputFormats(Format in) {
        AudioFormat af = (AudioFormat) in;
        supportedOutputFormats = new AudioFormat[] { new AudioFormat(Constants.SPEEX_RTP, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getEndian(), af.getSigned()) };
        return supportedOutputFormats;
    }
