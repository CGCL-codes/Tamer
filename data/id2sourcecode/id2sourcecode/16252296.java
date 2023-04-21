    public SimpleTemplate(URL url) throws IOException {
        String template = getStringFromStream(url.openStream());
        init(template);
    }
