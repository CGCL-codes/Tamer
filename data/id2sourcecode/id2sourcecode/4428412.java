    private AudioFormat getFormat(AudioInputStream stream) {
        Encoding encoding = stream.getFormat().getEncoding();
        if (encoding == Encoding.ALAW) {
            return (AudioFormat) AVProfile.PCMA;
        } else if (encoding == Encoding.ULAW) {
            return (AudioFormat) AVProfile.PCMU;
        } else if (encoding == Encoding.PCM_SIGNED) {
            int sampleSize = stream.getFormat().getSampleSizeInBits();
            int sampleRate = (int) stream.getFormat().getSampleRate();
            int channels = stream.getFormat().getChannels();
            return new AudioFormat(AudioFormat.LINEAR, sampleRate, sampleSize, channels, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED);
        }
        return null;
    }
