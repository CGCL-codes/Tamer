    protected synchronized String getMessage() {
        log.debug("getMessage");
        while (writerThread.isAlive()) {
            try {
                Thread.currentThread().wait(200L);
            } catch (InterruptedException e) {
            }
        }
        return message;
    }
