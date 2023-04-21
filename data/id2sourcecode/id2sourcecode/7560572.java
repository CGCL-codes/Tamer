    public boolean producerBegin(RenderSource source) throws IOException {
        prMinChans = Math.min(source.numAudioChannels, isf.getChannelNum());
        prBuf = new float[prMinChans][];
        return super.producerBegin(source);
    }
