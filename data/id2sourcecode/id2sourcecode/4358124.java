    public URLResourceBundle(String url) throws MalformedURLException, IOException {
        super(new URL(url).openStream());
    }
