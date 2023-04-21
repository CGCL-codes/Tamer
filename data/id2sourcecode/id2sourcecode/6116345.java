    public void write(DataWriter hdr) {
        buf.seek(bufStart);
        hdr.writeChunk(buf.readChunk(bufSize));
    }
