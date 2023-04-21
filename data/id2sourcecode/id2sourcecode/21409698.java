    public void test_getChannel_Append() throws IOException {
        File tmpfile = File.createTempFile("FileOutputStream", "tmp");
        tmpfile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tmpfile, true);
        assertEquals(0, fos.getChannel().position());
        fos.write(bytes);
        assertEquals(10, fos.getChannel().position());
        fos.write(bytes);
        assertEquals(20, fos.getChannel().position());
        fos.write(bytes);
        assertEquals(30, fos.getChannel().position());
        fos.close();
        try {
            fos.getChannel().position();
            fail("should throw ClosedChannelException");
        } catch (java.nio.channels.ClosedChannelException e) {
        }
    }
