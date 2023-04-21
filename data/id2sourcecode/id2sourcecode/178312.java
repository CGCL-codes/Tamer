    static void increaseMajor(String cfile, int delta) {
        try {
            RandomAccessFile cls = new RandomAccessFile(new File(System.getProperty("test.classes", "."), cfile), "rw");
            FileChannel fc = cls.getChannel();
            ByteBuffer rbuf = ByteBuffer.allocate(2);
            fc.read(rbuf, 6);
            ByteBuffer wbuf = ByteBuffer.allocate(2);
            wbuf.putShort(0, (short) (rbuf.getShort(0) + delta));
            fc.write(wbuf, 6);
            fc.force(false);
            cls.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed: unexpected exception");
        }
    }
