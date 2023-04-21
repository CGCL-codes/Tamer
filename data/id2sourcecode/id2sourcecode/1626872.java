    public void testNonExistingJarFileURL() throws Exception {
        String urlStr = "jar:" + testModuleFile.toURI().toString() + "xxx!/";
        URL url = new URL(urlStr);
        JarURLConnection con = (JarURLConnection) url.openConnection();
        JarURLConnection conFile = new JarFileURLConnection(url);
        Assert.assertEquals(con.getLastModified(), con.getLastModified());
        IOException error = null;
        try {
            con.getJarFile();
        } catch (ZipException x) {
            error = x;
        } catch (FileNotFoundException x) {
            error = x;
        }
        assertNotNull(error);
        error = null;
        try {
            conFile.getJarFile();
        } catch (ZipException x) {
            error = x;
        } catch (FileNotFoundException x) {
            error = x;
        }
        assertNotNull(error);
    }
