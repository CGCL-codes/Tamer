    public int read(int blockNumber, int sliceOffset, ByteBuffer buf) throws IOException {
        if (!isOpen()) open();
        if (T.t) T.ass(isOpen(), "File not open!");
        if (T.t) T.ass(isBlockComplete(blockNumber), "Block is not complete and we're trying to send it to a upload!");
        raf.seek(getOffset(blockNumber) + sliceOffset);
        return raf.getChannel().read(buf);
    }
