    protected URLConnection openConnection(URL url) throws IOException {
        return new FingerURLConnection(url);
    }
