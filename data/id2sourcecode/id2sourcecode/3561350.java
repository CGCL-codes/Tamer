    protected String readFrom(URL url) throws ProtocolException, IOException {
        Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("www-proxy.ikea.com", 8080));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        conn.setRequestMethod("GET");
        conn.setAllowUserInteraction(false);
        conn.setUseCaches(false);
        conn.setDoOutput(false);
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        String res = readEverything(is);
        conn.disconnect();
        return res;
    }
