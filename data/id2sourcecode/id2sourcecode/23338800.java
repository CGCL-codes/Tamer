    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getEntryName", args = {  })
    public void test_getEntryName() throws Exception {
        URL u = createContent("lf.jar", "plus.bmp");
        juc = (JarURLConnection) u.openConnection();
        assertEquals("Returned incorrect entryName", "plus.bmp", juc.getEntryName());
        u = createContent("lf.jar", "");
        juc = (JarURLConnection) u.openConnection();
        assertNull("Returned incorrect entryName", juc.getEntryName());
        URL url = new URL("jar:file:///bar.jar!/foo.jar!/Bugs/HelloWorld.class");
        assertEquals("foo.jar!/Bugs/HelloWorld.class", ((JarURLConnection) url.openConnection()).getEntryName());
    }
