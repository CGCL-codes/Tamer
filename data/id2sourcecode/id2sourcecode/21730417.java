    public static long padLogFile(FileOutputStream f, long currentSize, long preAllocSize) throws IOException {
        long position = f.getChannel().position();
        if (position + 4096 >= currentSize) {
            currentSize = currentSize + preAllocSize;
            fill.position(0);
            f.getChannel().write(fill, currentSize - fill.remaining());
        }
        return currentSize;
    }
