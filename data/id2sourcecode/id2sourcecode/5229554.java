    protected BytesInterface openFile(String filename) {
        try {
            File file = new File(homeDir + File.separatorChar + filename);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();
            return ChunkedMemoryMappedFile.mapFile(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
