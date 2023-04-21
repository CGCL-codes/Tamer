    public static URLConnection createConnection(final URL pUrl) throws IOException {
        final URLConnection urlConn = pUrl.openConnection();
        if (urlConn instanceof HttpURLConnection) {
            final HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setRequestMethod("POST");
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setDefaultUseCaches(false);
        return urlConn;
    }
