    public BlockBasedQueue(Container container, int blockSize, Function newObject, Function inputBufferSize, Function outputBufferSize, int size, int bytes, Object readBlockId, int readBlockOffset, Object writeBlockId, int writeBlockOffset) {
        this(container, blockSize, ConvertableConverter.DEFAULT_INSTANCE, newObject, inputBufferSize, outputBufferSize, size, bytes, readBlockId, readBlockOffset, writeBlockId, writeBlockOffset);
    }
