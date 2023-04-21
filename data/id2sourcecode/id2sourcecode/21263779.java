    public HttpUrlConnectionCall(HttpClientHelper helper, String method, String requestUri, boolean hasEntity) throws IOException {
        super(helper, method, requestUri);
        if (requestUri.startsWith("http")) {
            URL url = new URL(requestUri);
            this.connection = (HttpURLConnection) url.openConnection();
            int majorVersionNumber = SystemUtils.getJavaMajorVersion();
            int minorVersionNumber = SystemUtils.getJavaMinorVersion();
            if ((majorVersionNumber > 1) || ((majorVersionNumber == 1) && (minorVersionNumber >= 5))) {
                this.connection.setConnectTimeout(getHelper().getConnectTimeout());
                this.connection.setReadTimeout(getHelper().getReadTimeout());
            }
            this.connection.setAllowUserInteraction(getHelper().isAllowUserInteraction());
            this.connection.setDoOutput(hasEntity);
            this.connection.setInstanceFollowRedirects(getHelper().isFollowRedirects());
            this.connection.setUseCaches(getHelper().isUseCaches());
            this.responseHeadersAdded = false;
            if (this.connection instanceof HttpsURLConnection) {
                setConfidential(true);
                HttpsURLConnection https = (HttpsURLConnection) this.connection;
                SslContextFactory sslContextFactory = SslUtils.getSslContextFactory(getHelper());
                if (sslContextFactory != null) {
                    try {
                        SSLContext sslContext = sslContextFactory.createSslContext();
                        https.setSSLSocketFactory(sslContext.getSocketFactory());
                    } catch (Exception e) {
                        throw new RuntimeException("Unable to create SSLContext.", e);
                    }
                }
                HostnameVerifier verifier = helper.getHostnameVerifier();
                if (verifier != null) {
                    https.setHostnameVerifier(verifier);
                }
            }
        } else {
            throw new IllegalArgumentException("Only HTTP or HTTPS resource URIs are allowed here");
        }
    }
