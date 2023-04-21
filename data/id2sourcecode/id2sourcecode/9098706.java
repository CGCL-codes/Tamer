    public Object define(URL url) throws IOException {
        return define(classLoader.parseClass(url.openStream()));
    }
