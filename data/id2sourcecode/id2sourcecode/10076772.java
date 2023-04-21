    @TestTargets({ @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "putNextEntry", args = { java.util.zip.ZipEntry.class }), @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "JarOutputStream", args = { java.io.OutputStream.class, java.util.jar.Manifest.class }), @TestTargetNew(level = TestLevel.ADDITIONAL, clazz = Runtime.class, method = "exec", args = { java.lang.String[].class }) })
    public void test_execCreatedJarWithManifest() throws IOException, InterruptedException {
        File jarFile = File.createTempFile("cts_dalvikExecTest_", ".jar");
        jarFile.deleteOnExit();
        Manifest manifest = new Manifest();
        Attributes attrs = manifest.getMainAttributes();
        attrs.put(Attributes.Name.MANIFEST_VERSION, "3.1415962");
        attrs.put(Attributes.Name.MAIN_CLASS, "dalvikExecTest.HelloWorld");
        attrs.put(Attributes.Name.CLASS_PATH, jarFile.getName());
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jarFile), manifest);
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
        JarFile jarIn = new JarFile(jarFile);
        manifest = jarIn.getManifest();
        attrs = manifest.getMainAttributes();
        assertEquals("MANIFEST_VERSION must match!", "3.1415962", attrs.get(Attributes.Name.MANIFEST_VERSION));
        assertEquals("MAIN_CLASS must match!", "dalvikExecTest.HelloWorld", attrs.get(Attributes.Name.MAIN_CLASS));
        assertEquals("CLASS_PATH must match!", jarFile.getName(), attrs.get(Attributes.Name.CLASS_PATH));
    }
