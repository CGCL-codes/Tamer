    public static URLConnection openConnection(URL url, int timeout, String clientCertAlias, int hostCertLevel) throws IOException {
        URLConnector uc = new URLConnector(url, clientCertAlias, hostCertLevel);
        return uc.openConnection(timeout);
    }
