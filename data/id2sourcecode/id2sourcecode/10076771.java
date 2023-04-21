    @TestTargets({ @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "putNextEntry", args = { java.util.zip.ZipEntry.class }), @TestTargetNew(level = TestLevel.ADDITIONAL, method = "JarOutputStream", args = { java.io.OutputStream.class }), @TestTargetNew(level = TestLevel.ADDITIONAL, notes = "Create a temp file, fill it with contents according to Dalvik JAR format, and execute it on dalvikvm using -classpath option.", clazz = Runtime.class, method = "exec", args = { java.lang.String[].class }) })
    public void test_execCreatedJar() throws IOException, InterruptedException {
        File jarFile = File.createTempFile("cts_dalvikExecTest_", ".jar");
        jarFile.deleteOnExit();
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jarFile));
        jarOut.putNextEntry(new JarEntry("classes.dex"));
        Support_Resources.writeResourceToStream("cts_dalvikExecTest_classes.dex", jarOut);
        jarOut.putNextEntry(new JarEntry("dalvikExecTest/myResource"));
        jarOut.write("This Resource contains some text.".getBytes());
        jarOut.close();
        String res;
        res = execDalvik(jarFile.getAbsolutePath(), "dalvikExecTest.HelloWorld");
        assertEquals("Hello Android World!", "Hello Android World!\n", res);
        res = execDalvik(jarFile.getAbsolutePath(), "dalvikExecTest.ResourceDumper");
        assertTrue("Android Resource Dumper started", res.contains("Android Resource Dumper started"));
        assertTrue("This Resource contains some text.", res.contains("This Resource contains some text."));
    }
