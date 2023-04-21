    private URLConnection makeURLcon(final String s) throws URISyntaxException, IOException {
        URI uri;
        URL url;
        uri = new URI(s);
        url = uri.toURL();
        URLConnection urlcon = url.openConnection();
        return urlcon;
    }
