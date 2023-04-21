    public javax.microedition.io.Connection openConnection(String name, int mode, boolean timeouts) throws IOException {
        if (!isAllowNetworkConnection()) {
            throw new IOException("No network");
        }
        URL url;
        try {
            url = new URL(name);
        } catch (MalformedURLException ex) {
            throw new IOException(ex.toString());
        }
        cn = url.openConnection();
        if (cn instanceof HttpURLConnection) {
            HttpURLConnection httpCn = ((HttpURLConnection) cn);
            httpCn.setInstanceFollowRedirects(false);
            httpCn.setUseCaches(false);
            httpCn.setIfModifiedSince(0);
        }
        cn.setDoOutput(true);
        if (cn instanceof HttpURLConnection) {
            ((HttpURLConnection) cn).setInstanceFollowRedirects(false);
        }
        return this;
    }
