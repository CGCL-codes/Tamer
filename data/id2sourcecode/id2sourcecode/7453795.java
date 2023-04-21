    public void testMapFile() throws Exception {
        byte[] testData = newByteArray(1024);
        ByteBuffer localBuf = ByteBuffer.wrap(testData);
        File file = File.createTempFile("TestIOUtil", ".tmp");
        file.deleteOnExit();
        FileOutputStream out = new FileOutputStream(file);
        out.write(testData);
        out.close();
        ByteBuffer fileBuf1 = BufferUtil.map(file, 0, file.length(), MapMode.READ_ONLY);
        assertEquals(localBuf.limit(), fileBuf1.limit());
        assertEquals(localBuf.capacity(), fileBuf1.capacity());
        for (int ii = 0; ii < testData.length; ii++) assertEquals("byte " + ii, localBuf.get(ii), fileBuf1.get(ii));
        try {
            fileBuf1.putInt(0, 0xA5A55A5A);
            fail("able to write to read-only mapping");
        } catch (ReadOnlyBufferException ex) {
        }
        ByteBuffer fileBuf2 = BufferUtil.map(file, 128, 16, MapMode.READ_WRITE);
        assertEquals(localBuf.getLong(128), fileBuf2.getLong(0));
        ByteBuffer fileBuf3 = BufferUtil.map(file, 0, file.length(), MapMode.READ_WRITE);
        fileBuf3.putInt(16, 0xA5A55A5A);
        assertEquals(0xA5A55A5A, fileBuf1.getInt(16));
    }
