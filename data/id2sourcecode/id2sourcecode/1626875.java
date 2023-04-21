    public void testEmptyJarEntryURL() throws Exception {
        String urlStr = "jar:" + testModuleFile.toURI().toString() + "!/";
        URL url = new URL(urlStr);
        JarURLConnection con = (JarURLConnection) url.openConnection();
        JarURLConnection conFile = new JarFileURLConnection(url);
        assertNull(con.getJarEntry());
        assertNull(conFile.getJarEntry());
    }
