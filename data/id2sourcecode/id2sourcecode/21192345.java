    public final void shutdown() {
        synchronized (LOCK) {
            if (shutdown) return;
            shutdown = true;
        }
        if (LOG.isDebugEnabled()) LOG.debug("Shutting down socket & streams for: " + this);
        shutdownImpl();
        try {
            getChannel().close();
        } catch (IOException ignored) {
        }
        reader.shutdown();
        writer.shutdown();
        if (connecter != null) connecter.shutdown();
        if (shutdownObserver != null) shutdownObserver.shutdown();
        NIODispatcher.instance().invokeLater(new Runnable() {

            public void run() {
                reader = new NoOpReader();
                writer = new NoOpWriter();
                connecter = null;
                shutdownObserver = null;
            }
        });
    }
