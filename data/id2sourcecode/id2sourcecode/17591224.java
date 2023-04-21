    public void write(Tag tag, RandomAccessFile raf) throws IOException {
        FileChannel fc = raf.getChannel();
        ByteBuffer tagBuffer = tc.convert(tag);
        if (!tagExists(raf)) {
            fc.position(fc.size());
            fc.write(tagBuffer);
        } else {
            fc.position(fc.size() - 128);
            fc.write(tagBuffer);
        }
    }
