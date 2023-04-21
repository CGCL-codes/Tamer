    public void open(int nc) throws IOException {
        logger.debug("opening " + nc + " channels");
        if (servers.length <= 1) {
            throw new IOException("to few servers");
        }
        channels = new SocketChannel[nc];
        int j = 1;
        for (int i = 0; i < channels.length; i++) {
            if (j >= servers.length) {
                j = 1;
            }
            channels[i] = cf.getChannel(servers[j], ports[j]);
            j++;
        }
    }
