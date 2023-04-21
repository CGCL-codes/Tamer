    public void transferFrom(FileChannel channel, long position, long count, ReadableByteChannel source) throws IOException {
        if (position > channel.size()) throw exceptionFactory.create("position [" + position + "] > channel.size() [" + channel.size() + "]");
        while (true) {
            final long amountWritten = channel.transferFrom(source, position, count);
            if (amountWritten == 0) Thread.yield();
            count -= amountWritten;
            if (count > 0) position += amountWritten; else if (count == 0) break; else throw exceptionFactory.create();
        }
    }
