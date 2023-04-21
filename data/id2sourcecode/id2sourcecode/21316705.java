    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new HttpsURLConnectionImpl(url, getDefaultPort());
    }
