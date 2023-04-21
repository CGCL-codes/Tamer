    public NewFormat(AudioFormat audioFormat) {
        super();
        frameRate = audioFormat.getFrameRate();
        frameSize = audioFormat.getFrameSize();
        numChannels = audioFormat.getChannels();
        isBigEndian = audioFormat.isBigEndian();
        sampleRate = audioFormat.getSampleRate();
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
    }
