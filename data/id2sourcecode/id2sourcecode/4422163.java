    protected LogEvent.Factory newFactory(String name) throws IOException {
        URL url = getResource(name);
        String source = getClass().getPackage().getName().replace('.', '/') + '/' + name;
        return description.newFactory(new InputStreamReader(url.openStream()), source);
    }
