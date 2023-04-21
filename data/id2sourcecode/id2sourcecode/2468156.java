    public static byte[][] readChannels(final AudioInputStream origStream) {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, origStream);
        int channels = ais.getFormat().getChannels();
        int sampleSizeInBits = ais.getFormat().getSampleSizeInBits();
        if (sampleSizeInBits % 8 != 0) {
            throw new UnsupportedOperationException();
        }
        int sampleSizeInBytes = sampleSizeInBits >>> 3;
        if (ais.getFrameLength() > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        int frameLength = (int) ais.getFrameLength();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AudioSystem.write(ais, RawAudioFileWriter.RAW, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] samples = baos.toByteArray();
        byte[][] channelsData = new byte[channels][];
        for (int channel = 0; channel < channels; channel++) {
            channelsData[channel] = new byte[sampleSizeInBytes * frameLength];
        }
        for (int i = 0, sampleOffset = 0; i < frameLength; i++) {
            for (int channel = 0; channel < channels; channel++) {
                for (int k = 0; k < sampleSizeInBytes; k++, sampleOffset++) {
                    int channelOffset = i * sampleSizeInBytes + k;
                    channelsData[channel][channelOffset] = samples[sampleOffset];
                }
            }
        }
        return channelsData;
    }
