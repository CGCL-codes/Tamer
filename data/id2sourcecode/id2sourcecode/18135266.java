    public static FixtureSource newYamlUrl(String url) throws IOException {
        return new YamlSource(new URL(url).openStream());
    }
