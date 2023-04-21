    public void testJarFileURL() throws Exception {
        String urlStr = "jar:" + testModuleFile.toURI().toString() + "!/";
        URL url = new URL(urlStr);
        JarURLConnection con = (JarURLConnection) url.openConnection();
        JarURLConnection conFile = new JarFileURLConnection(url);
        assertEquals(con.getLastModified(), con.getLastModified());
        assertNotNull(con.getJarFile());
        assertNotNull(conFile.getJarFile());
    }
