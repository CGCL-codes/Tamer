    public void test_position() throws IOException {
        File tmp = File.createTempFile("hmy", "tmp");
        tmp.deleteOnExit();
        RandomAccessFile f = new RandomAccessFile(tmp, "rw");
        FileChannel ch = f.getChannel();
        MappedByteBuffer mbb = ch.map(MapMode.READ_WRITE, 0L, 100L);
        ch.close();
        mbb.putInt(1, 1);
        mbb.position(50);
        mbb.putInt(50);
        mbb.flip();
        mbb.get();
        assertEquals(1, mbb.getInt());
        mbb.position(50);
        assertEquals(50, mbb.getInt());
    }
