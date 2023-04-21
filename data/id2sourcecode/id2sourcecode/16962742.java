    public JavaClass getJavaClass(final URL url, final String className) throws IOException {
        if (url == null) {
            throw new NullPointerException("url");
        }
        if (className == null) {
            throw new NullPointerException("className");
        }
        return this.getJavaClass(url.openStream(), className);
    }
