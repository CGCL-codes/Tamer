    protected URLConnection openConnection(URL url) throws IOException {
        return new ResURLConnection(url, entries);
    }
