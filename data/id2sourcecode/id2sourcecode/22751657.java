    public void seek(long pos) throws IOException {
        if (readwrite == null) throw new IOException("HTMLTransformer not opened for random access");
        readwrite.seek(pos);
    }
