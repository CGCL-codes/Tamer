    public boolean patternOccurrsOnce(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        int fileSize = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
        CharBuffer cb = decoder.decode(bb);
        if (grep(f, cb)) {
            fc.close();
            return true;
        } else {
            fc.close();
            return false;
        }
    }
