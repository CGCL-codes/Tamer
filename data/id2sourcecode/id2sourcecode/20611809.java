    private Properties getProperties(URL url) throws IOException, PatternGeneratorException {
        Properties properties = new Properties();
        InputStream in = url.openStream();
        properties.load(in);
        in.close();
        if (properties.isEmpty()) throw new PatternGeneratorException("Could not load properties: " + url);
        return properties;
    }
