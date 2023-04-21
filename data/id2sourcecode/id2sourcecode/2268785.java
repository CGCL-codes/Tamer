    public void test_getEntryName() throws Exception {
        URL u = new URL("jar:" + BASE + "!/plus.bmp");
        juc = (JarURLConnection) u.openConnection();
        assertEquals("Returned incorrect entryName", "plus.bmp", juc.getEntryName());
        u = new URL("jar:" + BASE + "!/");
        juc = (JarURLConnection) u.openConnection();
        assertNull("Returned incorrect entryName", juc.getEntryName());
        URL url = new URL("jar:file:///bar.jar!/foo.jar!/Bugs/HelloWorld.class");
        assertEquals("foo.jar!/Bugs/HelloWorld.class", ((JarURLConnection) url.openConnection()).getEntryName());
    }
