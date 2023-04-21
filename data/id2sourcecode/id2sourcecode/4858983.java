    public void test_getChannel() throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        assertEquals(0, fis.getChannel().position());
        int r;
        int count = 1;
        while ((r = fis.read()) != -1) {
            assertEquals(count++, fis.getChannel().position());
        }
        fis.close();
        try {
            fis.getChannel().position();
            fail("should throw ClosedChannelException");
        } catch (java.nio.channels.ClosedChannelException e) {
        }
        fis = new FileInputStream(fileName);
        assertEquals(0, fis.getChannel().position());
        byte[] bs = new byte[10];
        r = fis.read(bs);
        assertEquals(10, fis.getChannel().position());
        fis.close();
        fis = new FileInputStream(fileName);
        assertEquals(0, fis.getChannel().position());
        bs = new byte[10];
        fis.skip(100);
        assertEquals(100, fis.getChannel().position());
        r = fis.read(bs);
        assertEquals(110, fis.getChannel().position());
        fis.close();
    }
