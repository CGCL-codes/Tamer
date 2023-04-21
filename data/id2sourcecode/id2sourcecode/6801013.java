    public void transferUncompressed(OutputStream out) throws FileNotFoundException, IOException {
        DataReader in = getDataReader();
        long size = indexBlock.getDecompressedSize();
        if (!indexBlock.isCompressed()) {
            size = indexBlock.getSize();
        }
        while (size > CHUNK_SIZE) {
            out.write(in.readChunk(CHUNK_SIZE));
            size -= CHUNK_SIZE;
        }
        while (size > 0) {
            out.write(in.readByte());
            size--;
        }
        if (in instanceof FileDataReader) {
            ((FileDataReader) in).close();
        }
    }
