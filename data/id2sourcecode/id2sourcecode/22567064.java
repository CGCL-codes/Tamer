    protected void copyFile(URL url, String dir, String name) throws IOException {
        copyFile(url.openStream(), dir, name);
    }
