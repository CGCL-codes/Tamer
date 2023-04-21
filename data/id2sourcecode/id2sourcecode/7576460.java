    private MultiMappedAudioStake(Span span, InterleavedStreamFile[] fs, Span[] fileSpans, Span[] maxFileSpans, int[][] channelMaps, String[] fileNames) {
        super(span);
        int numCh = 0;
        this.fs = fs;
        this.fileSpans = fileSpans;
        this.maxFileSpans = maxFileSpans;
        this.channelMaps = channelMaps;
        mappedDatas = new float[fs.length][][];
        for (int i = 0; i < fs.length; i++) {
            mappedDatas[i] = new float[fs[i].getChannelNum()][];
            numCh += channelMaps[i].length;
        }
        this.numChannels = numCh;
        this.fileNames = fileNames;
    }
