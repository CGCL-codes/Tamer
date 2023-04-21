    private InputStream getInputStreamForFile(String filename) throws IOException {
        try {
            URL url = new URL(filename);
            return url.openStream();
        } catch (MalformedURLException e) {
            return new FileInputStream(filename);
        }
    }
