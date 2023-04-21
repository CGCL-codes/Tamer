    private MappedByteBuffer createMappedBuffer(long file_offset, int length) throws IOException {
        if (channel == null) {
            FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, file_offset, length);
            if (access_mode == MODE_READ_ONLY) fc.close(); else channel = fc;
            return mbb;
        }
        return channel.map(FileChannel.MapMode.READ_WRITE, file_offset, length);
    }
