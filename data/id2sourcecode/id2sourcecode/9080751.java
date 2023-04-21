    protected java.net.URLConnection openConnection(java.net.URL url) throws java.io.IOException {
        return new JPackitURLConnection(context, url);
    }
