    private static void startNewThread() {
        writeThread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    timerLock.lock();
                    try {
                        if (condition.await(StatLogSettings.getIntervalUnit(), TimeUnit.SECONDS)) log.debug("await returns true");
                    } catch (Exception e) {
                        log.error("wait error", e);
                    } finally {
                        timerLock.unlock();
                    }
                    StatLog.write();
                }
            }
        }, StatLogSettings.WRITETHREAD_NAME);
        writeThread.start();
    }
