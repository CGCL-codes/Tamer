    private HttpURLConnection makeHttpsConnection(String path, String method) throws IOException {
        final StringBuffer urlSb = new StringBuffer();
        urlSb.append(protocol).append("://").append(host);
        if (port > 0) {
            urlSb.append(":").append(port);
        }
        urlSb.append("/vessel-servlet/");
        urlSb.append(path);
        if (logger.isDebugEnabled()) {
            logger.debug("making " + method + " connection to " + urlSb);
        }
        final URL url = new URL(urlSb.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestMethod(method);
        con.setRequestProperty("CONTENT_TYPE", "application/octet-stream");
        if (method.equals("PUT")) {
            con.setDoOutput(true);
        }
        con.setDoInput(true);
        return con;
    }
