    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new ClassPathURLConnection(url, _class);
    }
