    private InputStream openAuthorizedStream(URL url, String user, String passwd) throws IOException {
        String s = user + ":" + passwd;
        String base64 = "Basic " + new sun.misc.BASE64Encoder().encode(s.getBytes());
        URLConnection conn;
        if (StringUtils.isBlank(this.configProxyIp)) {
            conn = url.openConnection();
        } else {
            SocketAddress address = new InetSocketAddress(this.configProxyIp, this.configProxyPort);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            conn = url.openConnection(proxy);
        }
        conn.setRequestProperty("Authorization", base64);
        conn.connect();
        return conn.getInputStream();
    }
