    public void testNewInputStreamReadableByteChannel_InputNull() throws Exception {
        byte[] readbuf = new byte[this.testNum];
        this.fins = new FileInputStream(tmpFile);
        ReadableByteChannel readbc = this.fins.getChannel();
        assertEquals(this.fileSize, this.fins.available());
        assertTrue(readbc.isOpen());
        InputStream testins = Channels.newInputStream(null);
        assertNotNull(testins);
        try {
            testins.read(readbuf);
            fail();
        } catch (NullPointerException e) {
        }
        assertEquals(0, testins.available());
        try {
            testins.close();
            fail();
        } catch (NullPointerException e) {
        }
    }
