    public AudioInputStream getAudioInputStream(Encoding targetEncoding, AudioInputStream sourceStream) {
        if (sourceStream.getFormat().getEncoding().equals(targetEncoding)) return sourceStream;
        AudioFormat format = sourceStream.getFormat();
        int channels = format.getChannels();
        Encoding encoding = targetEncoding;
        float samplerate = format.getSampleRate();
        int bits = format.getSampleSizeInBits();
        boolean bigendian = format.isBigEndian();
        if (targetEncoding.equals(AudioFloatConverter.PCM_FLOAT)) bits = 32;
        AudioFormat targetFormat = new AudioFormat(encoding, samplerate, bits, channels, channels * bits / 8, samplerate, bigendian);
        return getAudioInputStream(targetFormat, sourceStream);
    }
