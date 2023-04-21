    public void test_close() throws Exception {
        try {
            zos.close();
            fail("Close on empty stream failed to throw exception");
        } catch (ZipException e) {
        }
        zos = new ZipOutputStream(bos);
        zos.putNextEntry(new ZipEntry("XX"));
        zos.closeEntry();
        zos.close();
        ZipOutputStream zos = new ZipOutputStream(new ByteArrayOutputStream());
        zos.putNextEntry(new ZipEntry("myFile"));
        zos.close();
        zos.close();
    }
