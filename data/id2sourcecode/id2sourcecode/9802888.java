    public WavWriter(DataOutput os, StreamInfo streamInfo) {
        this.os = os;
        this.osLE = new LittleEndianDataOutput(os);
        this.totalSamples = streamInfo.getTotalSamples();
        this.channels = streamInfo.getChannels();
        this.bps = streamInfo.getBitsPerSample();
        this.sampleRate = streamInfo.getSampleRate();
    }
