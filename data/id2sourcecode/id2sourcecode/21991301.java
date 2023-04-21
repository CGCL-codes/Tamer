    @SuppressWarnings({ "UnusedDeclaration" })
    private static java.nio.ByteBuffer memoryMapFile(java.io.File file) throws IOException {
        FileChannel roChannel = new RandomAccessFile(file, "r").getChannel();
        long fileSize = roChannel.size();
        MappedByteBuffer mapFile = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
        if (!mapFile.isLoaded()) mapFile.load();
        roChannel.close();
        return mapFile;
    }
