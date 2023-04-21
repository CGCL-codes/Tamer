        public void run() {
            Runnable run = null;
            RequestEntry entry = null;
            Logger logger = DasLogger.getLogger(DasLogger.SYSTEM_LOG);
            while (run == null) {
                synchronized (this) {
                    entry = (RequestEntry) list.getFirst();
                    if (entry.async && readCount == 0 && writer == null) {
                        list.removeFirst();
                        writer = entry;
                        run = entry.run;
                    } else if (!entry.async && writer == null) {
                        list.removeFirst();
                        readCount++;
                        run = entry.run;
                    }
                }
            }
            logger.fine("Starting :" + run);
            run.run();
            logger.fine("Finished :" + run);
            synchronized (this) {
                if (entry.async) {
                    writer = null;
                } else {
                    readCount--;
                }
                notifyAll();
            }
        }
