        @Override
        public void run() {
            long lastCheckTime = System.currentTimeMillis();
            syslog.log(Level.CONFIG, "{0} started at {1}", new Object[] { Thread.currentThread().getName(), Long.valueOf(lastCheckTime) });
            while (keepRunning) {
                try {
                    Thread.sleep(writerCheckInterval());
                    if (syslog.isLoggable(Level.FINE)) {
                        syslog.log(Level.FINE, "{0} running at {1} (last map change at {2})", new Object[] { Thread.currentThread().getName(), Long.valueOf(lastCheckTime), Long.valueOf(urlMapLastChange) });
                    }
                    if (urlMapLastChange > lastCheckTime) {
                        saveMap();
                    }
                    lastCheckTime = System.currentTimeMillis();
                } catch (InterruptedException e) {
                }
            }
        }
