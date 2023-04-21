    private MappedByteBuffer makeMappedBuffer(RandomAccessFile file) {
        FileChannel fc = file.getChannel();
        try {
            return fc.map(MapMode.READ_ONLY, 0, fc.size());
        } catch (IOException ioe) {
            throw new OperationFailedException("creating mapped buffer", ioe);
        }
    }
