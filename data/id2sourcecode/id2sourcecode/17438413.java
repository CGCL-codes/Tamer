    public void setProxy(String pacFile) throws Exception {
        setProxy((Proxy) null);
        File localFile = new File(pacFile);
        URL url;
        if (localFile.canRead()) {
            url = localFile.toURI().toURL();
        } else {
            url = new URI(pacFile).toURL();
        }
        URLConnection conn = url.openConnection();
        PacProxySelector a = new PacProxySelector(new BufferedReader(new InputStreamReader(conn.getInputStream())));
        Proxy proxy = a.select(new URI(CoreConfig.rootURL)).get(0);
        setProxy(proxy);
        proxyFile = pacFile;
    }
