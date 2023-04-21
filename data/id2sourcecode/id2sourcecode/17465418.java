    public void testNewInputStreamReadableByteChannel() throws Exception {
        ByteBuffer readbcbuf = ByteBuffer.allocateDirect(this.testNum);
        byte[] readbuf = new byte[this.testNum];
        this.fins = new FileInputStream(tmpFile);
        ReadableByteChannel readbc = this.fins.getChannel();
        assertEquals(this.fileSize, this.fins.available());
        assertTrue(readbc.isOpen());
        InputStream testins = Channels.newInputStream(readbc);
        testins.read(readbuf);
        assertEquals(this.fins.available(), this.fileSize - this.testNum);
        int readNum = readbc.read(readbcbuf);
        assertEquals(readNum, this.testNum);
        assertEquals(this.fins.available(), this.fileSize - this.testNum * 2);
        testins.read(readbuf);
        assertEquals(this.fins.available(), this.fileSize - this.testNum * 3);
        readbc.close();
        assertFalse(readbc.isOpen());
        try {
            testins.read(readbuf);
            fail();
        } catch (ClosedChannelException e) {
        }
    }
