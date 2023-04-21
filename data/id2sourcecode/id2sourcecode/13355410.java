    public MassIndexer threadsForIndexWriter(int numberOfThreads) {
        if (numberOfThreads < 1) throw new IllegalArgumentException("numberOfThreads must be at least 1");
        this.writerThreads = numberOfThreads;
        return this;
    }
