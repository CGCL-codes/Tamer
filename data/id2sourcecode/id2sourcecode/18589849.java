    private void initConverter(AudioFormat inFormat) {
        lastFormat = inFormat;
        numberOfChannels = inFormat.getChannels();
        encoder = new SpeexEncoder();
        encoder.init(0, 4, (int) inFormat.getSampleRate(), 1);
    }
