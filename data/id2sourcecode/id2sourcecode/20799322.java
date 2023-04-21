    public void testasIntBuffer() throws IOException {
        FileInputStream fis = new FileInputStream(tmpFile);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer mmb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        int len = mmb.capacity();
        assertEquals("Got wrong number of bytes", 46, len);
        for (int i = 0; i < 26; i++) {
            byte b = mmb.get();
            assertEquals("Got wrong byte value", (byte) 'A' + i, b);
        }
        IntBuffer ibuffer = mmb.asIntBuffer();
        for (int i = 0; i < 5; i++) {
            int val = ibuffer.get();
            assertEquals("Got wrong int value", i + 1, val);
        }
        fc.close();
    }
