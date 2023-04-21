    public MemoryMappedCorpusArray(SymbolTable symbolTable, String binaryFileName) throws IOException {
        super(symbolTable);
        IntBuffer tmp;
        RandomAccessFile binaryFile = new RandomAccessFile(binaryFileName, "r");
        FileChannel binaryChannel = binaryFile.getChannel();
        int headerSize = 0;
        tmp = binaryChannel.map(FileChannel.MapMode.READ_ONLY, headerSize, 4).asIntBuffer().asReadOnlyBuffer();
        this.numberOfSentences = tmp.get();
        this.binarySentenceBuffer = binaryChannel.map(FileChannel.MapMode.READ_ONLY, (headerSize + 4), 4 * numberOfSentences).asIntBuffer().asReadOnlyBuffer();
        tmp = binaryChannel.map(FileChannel.MapMode.READ_ONLY, (headerSize + 4 + 4 * numberOfSentences), 4).asIntBuffer().asReadOnlyBuffer();
        this.numberOfWords = tmp.get();
        this.binaryCorpusBuffer = binaryChannel.map(FileChannel.MapMode.READ_ONLY, (headerSize + 4 + 4 * numberOfSentences + 4), 4 * numberOfWords).asIntBuffer().asReadOnlyBuffer();
    }
