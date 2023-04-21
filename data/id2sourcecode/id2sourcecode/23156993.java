    public MemoryMappedSuffixArray(String suffixesFileName, Corpus corpus, int maxCacheSize) throws IOException, ClassNotFoundException {
        super(corpus, new Cache<Pattern, MatchedHierarchicalPhrases>(maxCacheSize));
        RandomAccessFile binaryFile = new RandomAccessFile(suffixesFileName, "r");
        FileChannel binaryChannel = binaryFile.getChannel();
        IntBuffer tmp = binaryChannel.map(FileChannel.MapMode.READ_ONLY, 0, 4).asIntBuffer().asReadOnlyBuffer();
        size = tmp.get();
        if (size != corpus.size()) {
            throw new RuntimeException("Size of suffix array (" + size + ") size does not match size of corpus (" + corpus.size() + ")");
        }
        this.binarySuffixBuffer = binaryChannel.map(FileChannel.MapMode.READ_ONLY, 4, 4 * size).asIntBuffer().asReadOnlyBuffer();
    }
