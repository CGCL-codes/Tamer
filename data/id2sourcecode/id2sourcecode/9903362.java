    public static InputStream getInputStream(URL url) throws Exception {
        log.info("<<<  FileUtilities.getInputStream - url = '" + url.toString() + "'");
        URLConnection uc = url.openConnection();
        String proxyServer = StringUtilities.getNString(Properties.getProperty("application", "PROXY_SERVER"));
        String proxyPort = StringUtilities.getNString(Properties.getProperty("application", "PROXY_PORT"));
        String proxyAuthorization = StringUtilities.getNString(Properties.getProperty("application", "PROXY_AUTHORIZATION"));
        if (!proxyServer.trim().equals("")) {
            System.getProperties().put("http.proxyHost", proxyServer);
            System.getProperties().put("http.proxyPort", proxyPort);
            if (!proxyAuthorization.trim().equals("")) uc.setRequestProperty("Proxy-Authorization", "Basic " + proxyAuthorization);
            uc.setDoInput(true);
            uc.setDoOutput(true);
        }
        log.info("<<<  url.openConnection() ");
        return uc.getInputStream();
    }
