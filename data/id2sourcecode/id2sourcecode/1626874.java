    public void testNonExistingJarEntryURL() throws Exception {
        String urlStr = "jar:" + testModuleFile.toURI().toString() + "!/PUSTEFIX-INF/txt/commonXXX.xml";
        URL url = new URL(urlStr);
        JarURLConnection con = (JarURLConnection) url.openConnection();
        JarURLConnection conFile = new JarFileURLConnection(url);
        assertEquals(con.getLastModified(), con.getLastModified());
        FileNotFoundException error = null;
        try {
            con.getJarEntry();
        } catch (FileNotFoundException x) {
            error = x;
        }
        assertNotNull(error);
        error = null;
        try {
            conFile.getJarEntry();
        } catch (FileNotFoundException x) {
            error = x;
        }
        assertNotNull(error);
    }
