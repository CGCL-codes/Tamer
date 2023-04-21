    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Regression test.", method = "getJarFile", args = {  })
    public void test_getJarFile29() throws Exception {
        File jarFile = File.createTempFile("1+2 3", "test.jar");
        jarFile.deleteOnExit();
        JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
        out.putNextEntry(new ZipEntry("test"));
        out.closeEntry();
        out.close();
        JarURLConnection conn = (JarURLConnection) new URL("jar:file:" + jarFile.getAbsolutePath().replaceAll(" ", "%20") + "!/").openConnection();
        conn.getJarFile().entries();
    }
