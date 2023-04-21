    public InputStream getInputStream(ConditionalGetValues condGetValues, URL url) throws IOException {
        return getInputStream(condGetValues, url.openConnection());
    }
