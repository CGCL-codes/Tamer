    public void test_getJarFileURL() throws Exception {
        URL fileURL = new URL(BASE);
        URL u = new URL("jar:" + BASE + "!/plus.bmp");
        juc = (JarURLConnection) u.openConnection();
        assertEquals("Returned incorrect file URL", fileURL, juc.getJarFileURL());
        URL url = new URL("jar:file:///bar.jar!/foo.jar!/Bugs/HelloWorld.class");
        assertEquals("file:/bar.jar", ((JarURLConnection) url.openConnection()).getJarFileURL().toString());
    }
