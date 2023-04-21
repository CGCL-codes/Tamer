    private synchronized void execute() throws IOException {
        boolean ready = writer.flush();
        if (!ready) {
            boolean block = writer.isBlocking();
            if (!block && !closed) {
                scheduler.release();
            }
            scheduler.repeat();
        } else {
            scheduler.ready();
        }
    }
