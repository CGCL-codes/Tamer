    public AudioFileCacheInfo(InterleavedStreamFile f, int model, long numFrames) throws IOException {
        this(f.getFile().getName(), f.getFile().lastModified(), model, f.getChannelNum(), numFrames);
    }
