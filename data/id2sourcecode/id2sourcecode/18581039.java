    private float computeStreamDuration() {
        if (oggStream instanceof UncachedOggStream) return -1;
        int bytesPerSec = 2 * streamHdr.getChannels() * streamHdr.getSampleRate();
        int totalBytes = getOggTotalBytes(Integer.MAX_VALUE);
        return (float) totalBytes / bytesPerSec;
    }
