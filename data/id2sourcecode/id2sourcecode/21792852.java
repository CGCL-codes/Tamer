    protected Format[] getMatchingOutputFormats(Format in) {
        System.out.println("AVCODEC: getMatchingOutputFormats");
        if (in == null) return new Format[] { new AudioFormat(AudioFormat.LINEAR) };
        AudioFormat af = (AudioFormat) in;
        supportedOutputFormats = new AudioFormat[] { new WavAudioFormat(AudioFormat.LINEAR, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSizeInBits(), (int) (af.getFrameSizeInBits() * af.getSampleRate() / 8.0), af.getEndian(), af.getSigned(), (float) af.getFrameRate(), af.getDataType(), new byte[0]) };
        return supportedOutputFormats;
    }
