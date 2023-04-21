    public static MappedByteBuffer memoryMapFileChunk(File file, long filePositionStart, long filePositionEnd) {
        try {
            FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel();
            MappedByteBuffer buf = rwChannel.map(FileChannel.MapMode.READ_WRITE, filePositionStart, filePositionEnd - filePositionStart);
            rwChannel.close();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unhandled exception", e);
        }
    }
