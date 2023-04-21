    public WHAudioFormat getFormat() {
        if (null == this.format) {
            this.format = new WHAudioFormat(this.iChannelCount, this.iSampleRate, 16);
            this.decoder = new Decoder();
            this.sampleBuffer = new SampleBuffer(this.format.getSamplesPerSecond(), this.format.getChannelCount());
            this.decoder.setOutputBuffer(this.sampleBuffer);
        }
        return this.format;
    }
