    public StreamIterator create(URL url, Filter filter) throws IOException {
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException e) {
            f = new File(url.getPath());
        }
        if (f.isDirectory()) {
            return new FileIterator(f, filter);
        } else {
            return new JarIterator(url.openStream(), filter);
        }
    }
