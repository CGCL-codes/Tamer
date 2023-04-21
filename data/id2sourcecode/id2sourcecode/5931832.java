    public void testRandomAccessFile_String_String() throws IOException {
        f.createNewFile();
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "r");
        FileChannel fcr = raf.getChannel();
        try {
            fcr.lock(0L, Long.MAX_VALUE, false);
            fail("NonWritableChannelException expected!");
        } catch (NonWritableChannelException e) {
        }
        raf.close();
    }
