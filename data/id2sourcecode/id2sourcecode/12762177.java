    public static HttpURLConnection getValidConnection(URL url) {
        HttpURLConnection httpurlconnection = null;
        try {
            URLConnection urlconnection = url.openConnection();
            urlconnection.connect();
            if (!(urlconnection instanceof HttpURLConnection)) {
                return null;
            }
            httpurlconnection = (HttpURLConnection) urlconnection;
        } catch (IOException ioexception) {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
            return null;
        }
        return httpurlconnection;
    }
