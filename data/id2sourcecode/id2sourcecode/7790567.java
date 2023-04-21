    public static IndexWriter newThreadedWriter(final Directory dir) throws IOException {
        final IndexWriter writer = new ThreadedIndexWriter(dir, getDefaultAnalyzer(), true, MaxFieldLength.UNLIMITED);
        return configWriter(writer);
    }
