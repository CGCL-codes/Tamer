    public static MappedByteBuffer createMemoryMappedFile(File file) {
        try {
            FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
            MappedByteBuffer buf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, rwChannel.size());
            rwChannel.close();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }
