    public void run() {
        try {
            sleep(INITIAL_SLEEP_INTERVAL);
        } catch (InterruptedException ie) {
        }
        while (running) {
            if (!loggedIn) {
                try {
                    supernodeConnector.connect();
                    supernode = (Worker2Supernode) getDefaultServer().getProxyFactory().createProxy(new URL(supernodeConnector.getSupernodeUrl() + "/" + Worker2Supernode.class.getSimpleName()), Worker2Supernode.class);
                    supernode.login(compPowerEstimator.getStaticProcessorParameters(), compPowerEstimator.estimateComputationPower());
                    latestReportBack = System.currentTimeMillis();
                    loggedIn = true;
                } catch (Exception e) {
                    System.out.println("failed to connect to supernode -- retrying ...");
                    if (debug) e.printStackTrace();
                }
            } else if (System.currentTimeMillis() - latestReportBack > reportBackInterval) {
                try {
                    try {
                        supernode.reportBack(compPowerEstimator.estimateComputationPower());
                        latestReportBack = System.currentTimeMillis();
                    } catch (PpException e) {
                        loggedIn = false;
                        System.out.println("error reporting back -> supernode connection lost");
                        if (debug) e.printStackTrace();
                    }
                } catch (Throwable t) {
                    loggedIn = false;
                    System.out.println("error determining cpu power -> not reporting back to supernode");
                    if (debug) t.printStackTrace();
                }
            }
            try {
                sleep(DAEMON_THREAD_SLEEP_INTERVAL);
            } catch (InterruptedException ie) {
            }
        }
        synchronized (this) {
            stopped = true;
            notify();
        }
    }
