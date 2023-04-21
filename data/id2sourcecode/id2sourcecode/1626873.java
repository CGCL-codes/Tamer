    public void testJarEntryURL() throws Exception {
        String urlStr = "jar:" + testModuleFile.toURI().toString() + "!/PUSTEFIX-INF/txt/common.xml";
        URL url = new URL(urlStr);
        JarURLConnection con = (JarURLConnection) url.openConnection();
        JarURLConnection conFile = new JarFileURLConnection(url);
        assertEquals(con.getContentLength(), conFile.getContentLength());
        assertNotNull(con.getJarEntry());
        assertNotNull(conFile.getJarEntry());
    }
