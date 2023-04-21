    private void init(String url_string, int request_method, boolean disable_trustmanager) throws java.lang.Exception {
        this.url = new URL(url_string);
        this.request_method = request_method;
        this.disable_trustmanager = disable_trustmanager;
        this.httpsUrlConn = null;
        this.BR = null;
        this.BW = null;
        this.BO = null;
        if (this.disable_trustmanager) {
            SSLContext sc;
            sc = SSLContext.getInstance("SSL");
            sc.init(null, this.getTrustManager(), new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        this.httpsUrlConn = (HttpsURLConnection) this.url.openConnection();
        if (this.request_method == PUT) {
            this.httpsUrlConn.setRequestMethod("PUT");
            this.httpsUrlConn.setDoOutput(true);
        } else if (this.request_method == DELETE) {
            this.httpsUrlConn.setRequestMethod("DELETE");
        }
    }
