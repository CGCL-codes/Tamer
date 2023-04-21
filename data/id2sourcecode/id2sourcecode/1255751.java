    public void flushBuffData() {
        this.pointBuff = new HashMap[layoutBuff.getChannelCount()];
        traceBuffRange2 = new int[layoutBuff.getChannelCount()][];
        traceBuff = new int[layoutBuff.getChannelCount()][];
    }
