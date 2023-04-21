    public static byte[] readChannel(final InputStream stream, final int channel) {
        final AudioInputStream audioStream;
        try {
            audioStream = AudioSystem.getAudioInputStream(stream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AudioInputStream sourceStream = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, audioStream);
        int channels = sourceStream.getFormat().getChannels();
        int sampleSizeInBits = sourceStream.getFormat().getSampleSizeInBits();
        if (sampleSizeInBits % 8 != 0) {
            throw new UnsupportedOperationException();
        }
        int sampleSizeInBytes = sampleSizeInBits >>> 3;
        if (sourceStream.getFrameLength() > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        int frameLength = (int) sourceStream.getFrameLength();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AudioSystem.write(sourceStream, RawAudioFileWriter.RAW, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] samples = baos.toByteArray();
        byte[] channelData = new byte[sampleSizeInBytes * frameLength];
        for (int i = 0, sampleOffset = 0; i < frameLength; i++) {
            for (int j = 0; j < channels; j++) {
                for (int k = 0; k < sampleSizeInBytes; k++, sampleOffset++) {
                    if (j == channel) {
                        int channelOffset = i * sampleSizeInBytes + k;
                        channelData[channelOffset] = samples[sampleOffset];
                    }
                }
            }
        }
        return channelData;
    }
