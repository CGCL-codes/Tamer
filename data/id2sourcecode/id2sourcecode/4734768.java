    private void checkProxyFeatures(CheckListener listener) {
        Server server = null;
        try {
            server = newServletServer(0, WorldServlet.class, "/", false);
        } catch (Exception e) {
            String message = "Failed to start servlet trivial server";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        }
        int serverPort = server.getConnectors()[0].getLocalPort();
        Server proxyServer = null;
        try {
            proxyServer = newServletServer(0, ProxyServlet.class, "/", false);
        } catch (Exception e) {
            String message = "Failed to start servlet proxy server";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        }
        int proxyPort = proxyServer.getConnectors()[0].getLocalPort();
        listener.checked(Category.PROXY, "Success starting servlet proxy server");
        URL url = null;
        byte[] buffer = new byte[512];
        int count = -1;
        try {
            url = new URL("http://127.0.0.1:" + serverPort + "/world");
            URLConnection connection = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", proxyPort)));
            InputStream stream = connection.getInputStream();
            count = stream.read(buffer);
            stream.close();
        } catch (MalformedURLException e) {
            String message = "Localhost 127.0.0.1 url is malformed by default";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        } catch (IOException e) {
            String message = "Proxy servlet container did not communicate properly";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        }
        String response = new String(buffer, 0, count);
        if (!WorldServlet.MESSAGE.equals(response)) {
            LOGGER.error(MessageFormat.format("Calling WorldServlet " + "container returns " + "erroneous response = [{0}]", response));
            String message = "Calling WorldServlet " + "container returns erroneous response";
            listener.failed(Category.PROXY, message, null);
            return;
        }
        listener.checked(Category.PROXY, "Success routing through servlet proxy server");
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Server.stop() failure", e);
        }
        try {
            proxyServer.stop();
        } catch (Exception e) {
            LOGGER.error("Proxy server.stop() failure", e);
        }
    }
