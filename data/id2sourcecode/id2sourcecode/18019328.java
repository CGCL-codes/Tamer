    public synchronized void close() throws IOException {
        boolean ready = writer.flush();
        if (!closed) {
            closed = true;
        }
        if (!ready) {
            scheduler.schedule(true);
        }
    }
