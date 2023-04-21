    private int getOggTotalBytes(int dataBytesTotal) {
        int numSamples;
        if (oggStream instanceof CachedOggStream) {
            CachedOggStream cachedOggStream = (CachedOggStream) oggStream;
            numSamples = (int) cachedOggStream.getLastOggPage().getAbsoluteGranulePosition();
        } else {
            UncachedOggStream uncachedOggStream = (UncachedOggStream) oggStream;
            numSamples = (int) uncachedOggStream.getLastOggPage().getAbsoluteGranulePosition();
        }
        int totalBytes = numSamples * streamHdr.getChannels() * 2;
        return Math.min(totalBytes, dataBytesTotal);
    }
