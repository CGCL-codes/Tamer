    private void initConverter(AudioFormat inFormat) {
        lastFormat = inFormat;
        numberOfChannels = inFormat.getChannels();
        decoder = new SpeexDecoder();
        decoder.init(0, (int) ((AudioFormat) inFormat).getSampleRate(), inFormat.getChannels(), false);
    }
