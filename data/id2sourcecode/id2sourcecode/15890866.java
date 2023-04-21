    public static URLConnection getConnection(String urlStr, SSLContext ctx) throws Exception {
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        if (con instanceof HttpsURLConnection) {
            HttpsURLConnection connection = (HttpsURLConnection) con;
            SSLSocketFactory factory = null;
            String proxyHost = null;
            int proxyPort = 0;
            proxyHost = getProxyHost();
            proxyPort = getProxyPort();
            factory = new SSLTunnelSocketFactory(ctx, proxyHost, proxyPort);
            connection.setSSLSocketFactory(factory);
        }
        con.setDoInput(true);
        con.setDoOutput(true);
        return con;
    }
