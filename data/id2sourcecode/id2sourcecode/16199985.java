    private void startMasterHeartBeat() {
        Runnable heartbeat = new Runnable() {

            private boolean jbossStarted = false;

            public void run() {
                while (true) {
                    if (!jbossStarted) {
                        try {
                            jbossStarted = (Boolean) localMbeanServer.getAttribute(new ObjectName("jboss.system", "type", "Server"), "Started");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (getState() != ServiceMBean.STARTED && getState() != ServiceMBean.STARTING) {
                            log.info("Master heartbeat thread stopped");
                            return;
                        }
                        try {
                            RandomAccessFile repositoryLockFile = new RandomAccessFile(repositoryHomeDir + "/.lock", "rw");
                            try {
                                FileLock lock = repositoryLockFile.getChannel().tryLock();
                                if (lock != null && lock.isValid()) {
                                    lock.release();
                                    startSlaveServer();
                                    return;
                                } else {
                                    log.trace("Master repository is alive");
                                }
                            } finally {
                                repositoryLockFile.close();
                            }
                        } catch (Exception e) {
                            log.error("Master heartbeat thread failed", e);
                        }
                    }
                    try {
                        Thread.sleep(masterHeartBeatInterval);
                    } catch (InterruptedException e) {
                        log.fatal("master heartbeat thread was interrupted", e);
                        return;
                    }
                }
            }
        };
        masterHeartBeatThread = new Thread(heartbeat, "Seamantics Master Heartbeat");
        masterHeartBeatThread.start();
    }
