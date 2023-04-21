    public ExternalSort(Comparator cmp, int maxSortSize, int mergeSize, int readBufferSize, int maxIndividualReadBufferSize, int writeBufferSize) {
        super();
        this.db = new BufferedObjectStore();
        this.maxSortSize = maxSortSize;
        this.mMergeSize = mergeSize;
        this.writeBufferSize = writeBufferSize;
        this.readBufferSize = readBufferSize;
        this.maxIndividualReadBufferSize = maxIndividualReadBufferSize;
        this.mComparator = cmp;
        this.data = new Object[this.maxSortSize];
        this.currentPos = 0;
        this.startTime = System.currentTimeMillis();
    }
