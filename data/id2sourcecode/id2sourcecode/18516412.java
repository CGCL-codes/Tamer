    public MappedByteBuffer mmap(long addr, long len) throws IOException {
        return raf.getChannel().map(MapMode.READ_WRITE, addr, len);
    }
