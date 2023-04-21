    public void testGenericConnectionThroughProxy() throws Exception {
        SocketAddress socket = new InetSocketAddress("proxy.zuehlke.com", 8080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, socket);
        URL url = new URL("http://www.google.com/codesearch/feeds/search?q=" + "System.out");
        URLConnection connection = url.openConnection(proxy);
        BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));
        String string;
        String contents = "";
        while ((string = in.readLine()) != null) {
            contents += string;
        }
        assertTrue(contents.length() > 0);
    }
