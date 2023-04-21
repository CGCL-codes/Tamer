    @Override
    public void run() {
        try {
            setStatus(MANAGERSTATUS.INITIALIZING);
            initialize();
            System.gc();
            if (getStatus() != MANAGERSTATUS.INITIALIZING) {
                return;
            }
            setStatus(MANAGERSTATUS.RUNNING);
            Thread th = new Thread(thGroup, writerQueue, "Output writer queue");
            th.setPriority(Thread.MAX_PRIORITY);
            th.setDaemon(true);
            th.start();
            doLoop();
        } catch (Throwable th) {
            setError(th);
            String m = th.getLocalizedMessage();
            if (m != null) {
                logger.log(Level.SEVERE, m, th);
            } else {
                logger.log(Level.SEVERE, "An error occurred: {0}", th.getClass().getSimpleName());
            }
        } finally {
            setStatus(MANAGERSTATUS.STOPPING);
            writerQueue.stop();
            writer.stop();
            disposeOpcDataProvider();
            setStatus(MANAGERSTATUS.STOPPED);
        }
    }
