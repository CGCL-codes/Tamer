    public URLConnection openConnection(URL url) throws IOException {
        BundleURLConnection conn = new BundleURLConnection(framework, url);
        return conn;
    }
