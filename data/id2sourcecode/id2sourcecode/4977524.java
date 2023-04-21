    public void shutdown() {
        logging.warning(LOG_NAME, "shutdown() is not tested.");
        connExecutor.shutdown();
        clientExecutor.shutdown();
        try {
            for (ConnEntry ent : connPool.values()) {
                ent.getChannel().close();
            }
            selector.close();
        } catch (IOException e) {
        }
    }
