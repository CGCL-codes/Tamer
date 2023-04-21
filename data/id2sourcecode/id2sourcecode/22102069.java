    public void test_closeEntry() throws IOException {
        ZipEntry ze = new ZipEntry("testEntry");
        ze.setTime(System.currentTimeMillis());
        zos.putNextEntry(ze);
        zos.write("Hello World".getBytes("UTF-8"));
        zos.closeEntry();
        assertTrue("closeEntry failed to update required fields", ze.getSize() == 11 && ze.getCompressedSize() == 13);
    }
