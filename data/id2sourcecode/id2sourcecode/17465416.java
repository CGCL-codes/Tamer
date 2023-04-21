    public void testNewChannelOutputStream() throws IOException {
        int writeNum = 0;
        ByteBuffer writebuf = ByteBuffer.allocateDirect(this.writebufSize);
        for (int val = 0; val < this.writebufSize / 2; val++) {
            writebuf.putChar((char) (val + 64));
        }
        this.fouts = new FileOutputStream(tmpFile);
        WritableByteChannel testChannel = this.fouts.getChannel();
        WritableByteChannel rbChannel = Channels.newChannel(this.fouts);
        assertTrue(testChannel.isOpen());
        assertTrue(rbChannel.isOpen());
        byte[] bit = new byte[1];
        bit[0] = 80;
        this.fouts.write(bit);
        this.fouts.flush();
        this.fins = new FileInputStream(tmpFile);
        assertEquals(this.fins.available(), 1);
        this.fins.close();
        writeNum = rbChannel.write(writebuf);
        assertEquals(0, writeNum);
        this.fouts.close();
        writeNum = rbChannel.write(writebuf);
        assertEquals(0, writeNum);
        try {
            writeNum = testChannel.write(writebuf);
            fail();
        } catch (ClosedChannelException e) {
        }
        assertEquals(0, writeNum);
        rbChannel.close();
        try {
            writeNum = testChannel.write(writebuf);
            fail();
        } catch (ClosedChannelException e) {
        }
    }
