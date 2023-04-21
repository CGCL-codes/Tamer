    @Override
    public String sendIp(String hostname, String ip) throws IOException {
        String request = bundle.getString("noip.url");
        String username = bundle.getString("noip.username");
        String password = bundle.getString("noip.password");
        String[] types = bundle.getString("noip.exceptions").split(",");
        Map<String, String> exceptions = new HashMap<String, String>(10);
        for (String type : types) {
            exceptions.put(bundle.getString("noip.exception." + type + ".name"), bundle.getString("noip.exception." + type + ".msg"));
        }
        request = request.replaceAll("username:password", username + ":" + password);
        request = request.replaceAll("hostname=hostname", "hostname=" + hostname);
        request = request.replaceAll("myip=myip", "myip=" + ip);
        Proxy proxy = null;
        String proxyName = bundle.getString("proxy.name");
        if (proxyName == null || proxyName.trim().length() == 0) {
            proxy = Proxy.NO_PROXY;
        } else {
            int proxyPort = Integer.parseInt(bundle.getString("proxy.port"));
            SocketAddress socket = new InetSocketAddress(proxyName, proxyPort);
            proxy = new Proxy(Proxy.Type.HTTP, socket);
        }
        URL url = new URL(request);
        URLConnection connection = url.openConnection(proxy);
        connection.setRequestProperty("Host", "dynupdate.no-ip.com");
        connection.setRequestProperty("User-Agent", "ip/0.1 labrouste.nas@gmail.com");
        connection.setRequestProperty("Authorization", username + ":" + password);
        connection.connect();
        InputStream stream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String code = buffer.readLine();
        buffer.close();
        reader.close();
        stream.close();
        buffer = null;
        reader = null;
        stream = null;
        connection = null;
        url = null;
        for (String exception : exceptions.keySet()) {
            if (exception.equalsIgnoreCase(code)) {
                throw new IOException(exceptions.get(exception));
            }
        }
        return code;
    }
