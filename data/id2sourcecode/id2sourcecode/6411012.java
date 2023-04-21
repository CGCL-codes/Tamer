    public SocketChannel getChannel(String h, int p) throws IOException {
        if (p <= 0) {
            p = port;
        }
        SocketChannel c = null;
        int i = 0;
        int delay = 5;
        logger.debug("connecting to " + h);
        while (c == null) {
            try {
                c = new SocketChannel(new Socket(h, p));
            } catch (IOException e) {
                i++;
                if (i % 50 == 0) {
                    delay += delay;
                    logger.info("Server on " + h + " not ready in " + delay + "ms");
                }
                System.out.println("Server on " + h + " not ready in " + delay + "ms");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException w) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during IO wait " + w);
                }
            }
        }
        logger.debug("connected, iter = " + i);
        return c;
    }
