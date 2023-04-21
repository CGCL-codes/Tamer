    private MappedByteBuffer getMemoryMappedFileStorage(long maxBytes, String fileName) throws IOException {
        this.fileName = fileName;
        fileStorage = new RandomAccessFile(fileName, "rw");
        fileStorage.seek(maxBytes);
        fileStorage.getChannel().map(PRIVATE, 0, maxBytes);
        return fileStorage.getChannel().map(PRIVATE, 0, maxBytes);
    }
