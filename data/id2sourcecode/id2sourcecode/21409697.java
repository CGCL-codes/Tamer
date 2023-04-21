    public void test_getChannel() throws IOException {
        File tmpfile = File.createTempFile("FileOutputStream", "tmp");
        tmpfile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tmpfile);
        fos.write(bytes);
        fos.flush();
        fos.close();
        FileOutputStream f = new FileOutputStream(tmpfile, true);
        assertEquals(10, f.getChannel().position());
    }
