    public HttpURLConnection getHttpURLConnection(final URL url) throws IOException {
        URLConnection urlConn = url.openConnection();
        if (urlConn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsUrlConn = (HttpsURLConnection) urlConn;
            if (this.hostnameVerifier != null) httpsUrlConn.setHostnameVerifier(this.hostnameVerifier);
            httpsUrlConn.setSSLSocketFactory(super.getSSLContext().getSocketFactory());
            return httpsUrlConn;
        } else if (urlConn instanceof HttpURLConnection) {
            return (HttpURLConnection) urlConn;
        } else return null;
    }
