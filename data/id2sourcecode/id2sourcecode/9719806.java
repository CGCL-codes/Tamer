    public MemoryMappedFileFilterInputStreamCache(File f) throws FileNotFoundException, IOException {
        if (f == null) {
            tempFile = File.createTempFile("MemoryMappedFileFilterInputStreamCache" + "_" + System.currentTimeMillis(), ".tmp");
            tempFile.deleteOnExit();
            f = tempFile;
        }
        this.raf = new RandomAccessFile(f, "rw");
        this.channel = raf.getChannel();
        this.buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, getMemoryMapSize());
    }
