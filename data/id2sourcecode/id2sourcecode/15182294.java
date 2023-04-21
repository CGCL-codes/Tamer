    static URLConnection openRemoteDescriptionFile(String urlstr) {
        URL url = null;
        try {
            url = new URL(urlstr);
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn;
        } catch (Exception e) {
            logger.error("Failed to retrive desc file:" + url, e);
            DesktopFrameworkConfiguration conf = DesktopFrameworkConfiguration.getInstance();
            SimpleSocketAddress localServAddr = conf.getLocalProxyServerAddress();
            Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(localServAddr.host, localServAddr.port));
            URLConnection conn;
            try {
                conn = url.openConnection(proxy);
                conn.connect();
                return conn;
            } catch (IOException e1) {
                logger.error("Failed to retrive desc file:" + url, e1);
            }
        }
        return null;
    }
