    public PCMDecoder(StreamInfo streamInfo) {
        this.totalSamples = streamInfo.getTotalSamples();
        this.channels = streamInfo.getChannels();
        this.bps = streamInfo.getBitsPerSample();
        this.sampleRate = streamInfo.getSampleRate();
        this.buf = new ByteData(streamInfo.getMaxFrameSize());
    }
