    public void test_finish() throws Exception {
        ZipEntry ze = new ZipEntry("test");
        zos.putNextEntry(ze);
        zos.write("Hello World".getBytes());
        zos.finish();
        assertEquals("Finish failed to closeCurrentEntry", 11, ze.getSize());
        ZipOutputStream zos = new ZipOutputStream(new ByteArrayOutputStream());
        zos.putNextEntry(new ZipEntry("myFile"));
        zos.finish();
        zos.close();
        try {
            zos.finish();
            fail("Assert 0: Expected IOException");
        } catch (IOException e) {
        }
    }
