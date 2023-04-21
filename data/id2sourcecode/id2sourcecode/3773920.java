    public URLReader(URL url) throws UnknownHostException, IOException {
        this.url = url;
        this.conn = (HttpURLConnection) url.openConnection();
        if (this.conn instanceof HttpsURLConnection) {
            allowSelfSignedCertificates();
        }
        this.conn.setDoInput(true);
        conn.setRequestMethod("GET");
    }
