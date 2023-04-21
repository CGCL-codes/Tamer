    private void findIgbLocalhostPort() {
        for (int i = 0; i < NUMBER_OF_PORTS_TO_TRY && igbPort == -1; i++) {
            int port = FIRST_PORT_TO_TRY + i;
            try {
                URL url = makePingUrl(port);
                URLConnection conn = url.openConnection();
                conn.connect();
                logger.debug("Found an igb port " + port);
                igbPort = port;
            } catch (MalformedURLException e) {
                logger.error("malformed url");
                return;
            } catch (IOException e) {
                logger.error("No port found at " + port, e);
            }
        }
    }
