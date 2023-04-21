    public long getFilePointer() throws IOException {
        if (readwrite == null) throw new IOException("HTMLTransformer not opened for random access");
        return readwrite.getFilePointer();
    }
