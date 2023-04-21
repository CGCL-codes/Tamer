    public static URLConnection createUnCertifiedConnection(URL url, Proxy proxy) throws IOException {
        if (sc == null) {
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, SSLUtil.DUMMY_TRUST_MANAGERS, new SecureRandom());
                URLUtil.sc = sc;
            } catch (Exception ex) {
                throw new ImpossibleException(ex);
            }
        }
        URLConnection con = proxy == null ? url.openConnection() : url.openConnection(proxy);
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection httpsCon = (HttpsURLConnection) con;
            httpsCon.setSSLSocketFactory(sc.getSocketFactory());
            httpsCon.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
        }
        return con;
    }
