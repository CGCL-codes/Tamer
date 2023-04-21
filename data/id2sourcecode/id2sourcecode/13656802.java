    private static Element parse(String resourcePath) throws IOException {
        URL url = NgreaseAcceptanceTest.class.getResource(resourcePath);
        ElementParser parser = new MetaParser();
        InputStream stream = url.openStream();
        Element result = parser.parse(stream);
        return result;
    }
