    private void checkContainerFeatures(CheckListener listener) {
        StreamyContextHandler handler = new StreamyContextHandler();
        handler.addServlet(HelloServlet.class, "/hello");
        Server server = new Server(0);
        server.setHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            String message = "Starting servlet container failed";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        }
        listener.checked(Category.PROXY, "Success starting servlet container");
        URL url = null;
        byte[] buffer = new byte[512];
        int count = -1;
        try {
            url = new URL("http://127.0.0.1:" + server.getConnectors()[0].getLocalPort() + "/hello");
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            count = stream.read(buffer);
            stream.close();
        } catch (MalformedURLException e) {
            String message = "Localhost 127.0.0.1 url is malformed by default";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        } catch (IOException e) {
            String message = "Servlet container did not communicate properly";
            LOGGER.error(message, e);
            listener.failed(Category.PROXY, message, e);
            return;
        }
        String response = new String(buffer, 0, count);
        if (!HelloServlet.MESSAGE.equals(response)) {
            String message = "Calling servlet container returns erroneous response";
            LOGGER.error(MessageFormat.format("Calling servlet container " + "returns erroneous response = [{0}]", message));
            listener.failed(Category.PROXY, message, null);
            return;
        }
        listener.checked(Category.PROXY, "Success calling servlet container");
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Server.stop() failure", e);
        }
    }
