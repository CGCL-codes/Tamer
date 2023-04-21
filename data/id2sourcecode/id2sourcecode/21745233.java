    private synchronized void block() throws InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug("Buffer size: " + String.valueOf(buf.length));
            log.debug("Unread data: " + String.valueOf(writepos - readpos));
        }
        if (!closed) {
            while ((readpos >= writepos) && !closed) {
                wait(interrupt);
            }
        }
    }
