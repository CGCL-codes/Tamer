    public void init() throws ServletException {
        initParameter();
        final List<MonitorTask> monitorTasks = new ArrayList<MonitorTask>();
        initMonitorTasks(monitorTasks);
        initWriter();
        if (null != writeThread) {
            try {
                writeThread.interrupt();
            } catch (Exception e) {
                log.error("interrupt write thread error", e);
            }
        }
        writeThread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    timerLock.lock();
                    try {
                        condition.await(intervalTime, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("wait error", e);
                    } finally {
                        timerLock.unlock();
                    }
                    for (MonitorTask task : monitorTasks) {
                        try {
                            task.start();
                        } catch (java.lang.Throwable t) {
                            log.error("task error" + task.getClass(), t);
                        }
                    }
                }
            }
        });
        writeThread.setName(WRITETHREAD_NAME);
        writeThread.start();
    }
