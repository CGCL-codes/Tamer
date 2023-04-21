    protected static HttpURLConnection openConnection(String baseURL, String queryString) throws MalformedURLException, IOException {
        final StringBuilder buff = new StringBuilder();
        buff.append(baseURL);
        if (queryString != null) {
            buff.append("?");
            buff.append(queryString);
        }
        final URL url = new URL(buff.toString());
        return (HttpURLConnection) url.openConnection();
    }
