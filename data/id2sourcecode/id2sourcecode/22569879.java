    @Override
    public String findIp() throws IOException {
        URL url = new URL(bundle.getString("ip.url"));
        Proxy proxy = null;
        String proxyName = bundle.getString("proxy.name");
        if (proxyName == null || proxyName.trim().length() == 0) {
            proxy = Proxy.NO_PROXY;
        } else {
            int proxyPort = Integer.parseInt(bundle.getString("proxy.port"));
            SocketAddress socket = new InetSocketAddress(proxyName, proxyPort);
            proxy = new Proxy(Proxy.Type.HTTP, socket);
        }
        URLConnection connection = url.openConnection(proxy);
        connection.connect();
        InputStream stream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String ip = buffer.readLine();
        buffer.close();
        reader.close();
        stream.close();
        buffer = null;
        reader = null;
        stream = null;
        connection = null;
        url = null;
        return ip;
    }
