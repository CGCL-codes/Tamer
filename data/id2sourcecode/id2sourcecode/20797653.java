    public void open() throws IOException {
        logger.debug("opening " + servers.length + " channels");
        if (servers.length <= 1) {
            throw new IOException("to few servers");
        }
        channels = new SocketChannel[servers.length - 1];
        for (int i = 1; i < servers.length; i++) {
            channels[i - 1] = cf.getChannel(servers[i], ports[i]);
        }
    }
