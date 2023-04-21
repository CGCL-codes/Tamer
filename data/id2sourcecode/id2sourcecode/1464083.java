    public Format[] getSupportedOutputFormats(Format in) {
        if (in == null) return new Format[] { new AudioFormat(AudioFormat.LINEAR) };
        AudioFormat af = (AudioFormat) in;
        return new Format[] { new WavAudioFormat(AudioFormat.LINEAR, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSizeInBits(), (int) (af.getFrameSizeInBits() * af.getSampleRate() / 8), af.getEndian(), af.getSigned(), (float) af.getFrameRate(), af.getDataType(), new byte[0]) };
    }
