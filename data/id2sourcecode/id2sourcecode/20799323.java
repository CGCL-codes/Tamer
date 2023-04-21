    public void testEmptyBuffer() throws IOException {
        FileInputStream fis = new FileInputStream(emptyFile);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer mmb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        assertNotNull("MappedByteBuffer created from empty file should not be null", mmb);
        int len = mmb.capacity();
        assertEquals("MappedByteBuffer created from empty file should have 0 capacity", 0, len);
        assertFalse("MappedByteBuffer from empty file shouldn't be backed by an array ", mmb.hasArray());
        try {
            byte b = mmb.get();
            fail("Calling MappedByteBuffer.get() on empty buffer should throw a BufferUnderflowException");
        } catch (BufferUnderflowException e) {
        }
        try {
            mmb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
            fail("Expected NonWritableChannelException to be thrown");
        } catch (NonWritableChannelException e) {
        }
        try {
            mmb = fc.map(FileChannel.MapMode.PRIVATE, 0, fc.size());
            fail("Expected NonWritableChannelException to be thrown");
        } catch (NonWritableChannelException e) {
        }
        fc.close();
    }
