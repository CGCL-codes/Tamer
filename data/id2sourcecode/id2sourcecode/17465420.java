    public void testNewOutputStreamWritableByteChannel() throws Exception {
        byte[] writebuf = new byte[this.testNum];
        ByteBuffer writebcbuf = ByteBuffer.allocateDirect(this.testNum);
        this.fouts = new FileOutputStream(tmpFile);
        WritableByteChannel writebc = this.fouts.getChannel();
        assertTrue(writebc.isOpen());
        OutputStream testouts = Channels.newOutputStream(writebc);
        testouts.write(writebuf);
        this.assertFileSizeSame(tmpFile, this.testNum);
        writebc.write(writebcbuf);
        this.assertFileSizeSame(tmpFile, this.testNum * 2);
        testouts.write(writebuf);
        this.assertFileSizeSame(tmpFile, this.testNum * 3);
        writebc.close();
        assertFalse(writebc.isOpen());
        try {
            testouts.write(writebuf);
            fail();
        } catch (ClosedChannelException e) {
        }
    }
