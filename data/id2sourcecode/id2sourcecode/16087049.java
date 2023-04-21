    private void add(Properties p, URL url) throws IOException {
        InputStream is = url.openStream();
        Properties properties = new Properties();
        properties.load(is);
        p.putAll(properties);
        try {
            is.close();
        } catch (IOException ex) {
        }
    }
