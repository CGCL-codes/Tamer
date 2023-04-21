    public void startAcceptingCommands() {
        log.debug("Start accepting connections");
        isFinished = false;
        writerThread.start();
        try {
            listenUDP();
        } catch (IOException ex) {
            log.error("Can't accept UDP connections", ex);
        }
        try {
            listenTCP();
        } catch (IOException ex) {
            log.error("Can't accept TCP connections", ex);
        }
    }
