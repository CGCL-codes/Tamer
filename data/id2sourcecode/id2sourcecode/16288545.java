    public ByteBufferIterator(String fN) throws IOException {
        fPath = fN;
        fis = new FileInputStream(fN);
        fc = fis.getChannel();
        fSize = fc.size();
    }
