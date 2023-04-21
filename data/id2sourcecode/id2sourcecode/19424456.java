    public static BufferReader load(File file, Mode mode) throws IOException {
        FileChannel channel = new RandomAccessFile(file, mode.rafMode()).getChannel();
        BufferReader b = new BufferReader(channel.map(mode.mapMode(), 0, (int) channel.size()));
        channel.close();
        b._buffer.load();
        return b;
    }
