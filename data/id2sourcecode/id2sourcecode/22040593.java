    public synchronized void listen(AudioSource audioSource, int channel, boolean useSpeechDetector) {
        releaseSampleBuffer();
        sampleBufferWriter = audioSource;
        format = audioSource.getFormat();
        sampleBuffer = new SampleBuffer(format.getChannels(), format.getFrameSize());
        int sampleRate = (int) format.getSampleRate();
        reader = sampleBuffer.reader(channel);
        reader.setBlocking(false);
        levelReader = sampleBuffer.reader(channel);
        levelReader.setBlocking(false);
        maxLevel = 0;
        resetLevel = false;
        if (this.sampleRate != sampleRate) {
            this.sampleRate = sampleRate;
            initialize();
        }
        maxval = Double.MIN_VALUE;
        maxeng = Double.MIN_VALUE;
        mineng = Double.MAX_VALUE;
        nextUttStartSample = sampleForByte(reader.position());
        uttEndSample = Long.MAX_VALUE;
        windowBytes.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        levelBytes.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        limit = sampleBuffer.createLimit(0);
        limitMax = 0;
        enable(useSpeechDetector);
    }
