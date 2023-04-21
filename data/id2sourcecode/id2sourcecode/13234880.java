    synchronized void newFileCleaner() {
        StringWriter msg = new StringWriter();
        if (activeFileCleaner != null && activeFileCleaner.isAlive()) {
            msg.write("Will NOT create a new FileCleaner thread while an old one is still running!\n");
            StackTraceElement[] dump = activeFileCleaner.getStackTrace();
            for (StackTraceElement s : dump) {
                msg.write("\t" + s.toString());
            }
            LOG.error(msg);
        } else {
            activeFileCleaner = new Thread(new FileCleaner());
            activeFileCleaner.setName("SJQ-FileCleaner");
            activeFileCleaner.setDaemon(true);
            activeFileCleaner.start();
        }
    }
