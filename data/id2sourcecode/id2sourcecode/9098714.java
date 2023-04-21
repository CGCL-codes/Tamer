    public Object build(Closure objectVisitor, URL url) throws IOException {
        return build(objectVisitor, classLoader.parseClass(url.openStream()));
    }
