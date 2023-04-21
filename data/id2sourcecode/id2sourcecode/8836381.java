    public AudioFormatComparator(AudioFormat desiredAudioFormat) {
        this.desiredAudioFormat = desiredAudioFormat;
        channels = desiredAudioFormat.getChannels();
        encoding = desiredAudioFormat.getEncoding();
        frameRate = desiredAudioFormat.getFrameRate();
        sampleRate = desiredAudioFormat.getSampleRate();
        sampleSizeInBits = desiredAudioFormat.getSampleSizeInBits();
        isBigEndian = desiredAudioFormat.isBigEndian();
    }
