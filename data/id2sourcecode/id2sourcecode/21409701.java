    public void test_getChannel_Unappend_Append() throws IOException {
        File tmpfile = File.createTempFile("FileOutputStream", "tmp");
        tmpfile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tmpfile, false);
        assertEquals(0, fos.getChannel().position());
        fos.write(bytes);
        assertEquals(10, fos.getChannel().position());
        fos.close();
        fos = new FileOutputStream(tmpfile, true);
        assertEquals(10, fos.getChannel().position());
        fos.close();
    }
