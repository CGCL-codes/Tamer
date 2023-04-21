    public BlockBasedQueue(Container container, int blockSize, Converter converter, Function inputBufferSize, Function outputBufferSize, int size, int bytes, Object readBlockId, int readBlockOffset, Object writeBlockId, int writeBlockOffset) {
        this(container, blockSize, converter, new Constant(null), inputBufferSize, outputBufferSize, size, bytes, readBlockId, readBlockOffset, writeBlockId, writeBlockOffset);
    }
