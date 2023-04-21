    public URLConnection openConnection(URL url) throws IOException {
        PluginResURLConnection c = new PluginResURLConnection(url);
        c.connect();
        return c;
    }
