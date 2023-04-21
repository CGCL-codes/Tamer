    public int writeToFile(int pointer, RandomAccessFile file, long position, int count) throws IOException {
        assert (buffer != null);
        assert (pointer >= 0);
        assert (pointer < buffer.capacity());
        assert (file != null);
        assert (position >= 0);
        assert (count > 0);
        buffer.limit(pointer + count).position(pointer);
        try {
            return file.getChannel().write(buffer, position);
        } finally {
            buffer.clear();
        }
    }
