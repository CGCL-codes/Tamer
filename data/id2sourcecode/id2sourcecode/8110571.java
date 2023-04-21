    private void loadProperties(String fileName, Properties properties) throws IOException {
        URL url = ClassLoader.getSystemResource(fileName);
        InputStream in = url.openStream();
        properties.load(in);
    }
