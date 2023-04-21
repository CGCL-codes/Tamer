    @Override
    public void write(WritableByteChannel writableChannel) throws IOException {
        FileChannel fc = getChannel();
        long position = 0;
        long count = fc.size();
        long written = 0;
        while (count > 0) {
            written = fc.transferTo(position, count, writableChannel);
            position += written;
            count -= written;
        }
    }
