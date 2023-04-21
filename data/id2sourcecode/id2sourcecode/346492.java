    public AudioArray(AudioInputStream stream) {
        sampleRate = (int) stream.getFormat().getSampleRate();
        sampleSizeInBits = stream.getFormat().getSampleSizeInBits();
        numChannels = stream.getFormat().getChannels();
        channelLength = (int) stream.getFrameLength();
        frameSize = stream.getFormat().getFrameSize();
        frameRate = stream.getFormat().getFrameRate();
        chs = new short[numChannels][channelLength];
        this.stream = stream;
        index = 0;
        channelBufferSize = 4096;
        loadAudioFile();
    }
