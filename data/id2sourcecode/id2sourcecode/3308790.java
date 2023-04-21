    private void createReportThread() {
        reportThread = new Thread(new Runnable() {

            public void run() {
                mainLoop: while (isRunning) {
                    synchronized (MonitorManager.this) {
                        while (pulseRate == NOT_PULSING) {
                            try {
                                MonitorManager.this.wait();
                            } catch (InterruptedException e) {
                                continue mainLoop;
                            }
                        }
                        while (pulseRate != NOT_PULSING) {
                            if (Thread.interrupted()) {
                                continue mainLoop;
                            }
                            long now = System.currentTimeMillis();
                            try {
                                long waitTime = nextPulseTime - now;
                                if (waitTime > 0) {
                                    MonitorManager.this.wait(nextPulseTime - now);
                                }
                                pulseNumber += pulsesPerRate[pulseRateIndex];
                                generateReports();
                                nextPulseTime += pulseRate;
                            } catch (InterruptedException e) {
                                if (pulseRateIndex == NOT_PULSING) {
                                    continue mainLoop;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, "Meter-Monitor-Report");
        reportThread.setDaemon(true);
        reportThread.start();
    }
