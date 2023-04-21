    public void testExternalFile() throws IOException {
        count++;
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource("expand-subdir.txt");
        copy(url.openStream(), null);
        InputStream is = this.getClass().getResourceAsStream("/expand-subdir.txt");
        copy(is, null);
    }
