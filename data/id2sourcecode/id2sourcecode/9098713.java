    public Object build(URL url) throws IOException {
        return build(classLoader.parseClass(url.openStream()));
    }
