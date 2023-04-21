    public MemoryMappedAlignmentGrids(String binaryAlignmentsFilename, Corpus sourceCorpus, Corpus targetCorpus, boolean requireTightSpans) throws IOException {
        super(sourceCorpus, targetCorpus, requireTightSpans);
        RandomAccessFile binaryFile = new RandomAccessFile(binaryAlignmentsFilename, "r");
        FileChannel binaryChannel = binaryFile.getChannel();
        IntBuffer tmp;
        int start = 0;
        int length = 4;
        tmp = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asIntBuffer().asReadOnlyBuffer();
        this.size = tmp.get();
        start += length;
        length = 4 * size;
        this.widths = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asIntBuffer().asReadOnlyBuffer();
        start += length;
        length = 4 * size;
        this.heights = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asIntBuffer().asReadOnlyBuffer();
        start += length;
        length = 4 * (size + 1);
        this.pointCounts = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asIntBuffer().asReadOnlyBuffer();
        int totalPoints = pointCounts.get(size);
        start += length;
        length = 2 * totalPoints;
        this.alignmentPoints = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asShortBuffer().asReadOnlyBuffer();
        start += length;
        length = 2 * totalPoints;
        this.reverseAlignmentPoints = binaryChannel.map(FileChannel.MapMode.READ_ONLY, start, length).asShortBuffer().asReadOnlyBuffer();
    }
