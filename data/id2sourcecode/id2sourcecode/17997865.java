    @Override
    public AudioFormat getFormat() {
        if (audioFormat == null) {
            try {
                decoder.decodeFrame(adts.readNextFrame(), sampleBuffer);
                audioFormat = new AudioFormat(sampleBuffer.getSampleRate(), sampleBuffer.getBitsPerSample(), sampleBuffer.getChannels(), true, true);
                saved = sampleBuffer.getData();
            } catch (IOException e) {
                return null;
            }
        }
        return audioFormat;
    }
