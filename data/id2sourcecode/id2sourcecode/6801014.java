    public void transferDirect(DataWriter out) throws IOException {
        DataReader in = getDataReaderMaintainCompression();
        in.seek(indexBlock.getLocation());
        long size = indexBlock.getDecompressedSize();
        if (indexBlock.isCompressed()) {
            size = indexBlock.getSize();
        }
        while (size > CHUNK_SIZE) {
            out.writeChunk(in.readChunk(CHUNK_SIZE));
            size -= CHUNK_SIZE;
        }
        while (size > 0) {
            out.writeByte(in.readByte());
            size--;
        }
        if (in instanceof FileDataReader) {
            ((FileDataReader) in).close();
        }
    }
