    protected void writeHeader() throws ContentEncodingException, IOException {
        putHeader(_baseName, lengthWritten(), getBlockSize(), _dh.digest(), null);
    }
