    public ByteBufferIterator(String fN, int buflen) throws Exception {
        fPath = fN;
        fis = new FileInputStream(fN);
        fc = fis.getChannel();
        fSize = fc.size();
        bufferSize = buflen;
    }
