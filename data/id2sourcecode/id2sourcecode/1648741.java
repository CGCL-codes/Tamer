    public void run() {
        long crtTime, timeRemained, nextOpTime;
        long nextRecheck = 0, nextJobInfoSend = 0, nextSysInfoSend = 0;
        int generalInfoCount;
        int nextOp = 0;
        boolean haveChange, haveTimeout;
        logger.info("[Starting background thread...]");
        crtTime = System.currentTimeMillis();
        synchronized (apm.mutexBack) {
            if (apm.confCheck) {
                nextRecheck = crtTime + apm.crtRecheckInterval * 1000;
            }
            if (apm.jobMonitoring) nextJobInfoSend = crtTime + apm.jobMonitorInterval * 1000;
            if (apm.sysMonitoring) nextSysInfoSend = crtTime + apm.sysMonitorInterval * 1000;
        }
        timeRemained = nextOpTime = -1;
        generalInfoCount = 0;
        while (hasToRun) {
            crtTime = System.currentTimeMillis();
            if (nextRecheck > 0 && (nextJobInfoSend <= 0 || nextRecheck <= nextJobInfoSend)) {
                if (nextSysInfoSend <= 0 || nextRecheck <= nextSysInfoSend) {
                    nextOp = RECHECK_CONF;
                    nextOpTime = nextRecheck;
                } else {
                    nextOp = SYS_INFO_SEND;
                    nextOpTime = nextSysInfoSend;
                }
            } else {
                if (nextJobInfoSend > 0 && (nextSysInfoSend <= 0 || nextJobInfoSend <= nextSysInfoSend)) {
                    nextOp = JOB_INFO_SEND;
                    nextOpTime = nextJobInfoSend;
                } else if (nextSysInfoSend > 0) {
                    nextOp = SYS_INFO_SEND;
                    nextOpTime = nextSysInfoSend;
                }
            }
            if (nextOpTime == -1) nextOpTime = crtTime + ApMon.RECHECK_INTERVAL * 1000;
            synchronized (apm.mutexCond) {
                synchronized (apm.mutexBack) {
                    haveChange = false;
                    if (apm.jobMonChanged || apm.sysMonChanged || apm.recheckChanged) haveChange = true;
                    if (apm.jobMonChanged) {
                        if (apm.jobMonitoring) nextJobInfoSend = crtTime + apm.jobMonitorInterval * 1000; else nextJobInfoSend = -1;
                        apm.jobMonChanged = false;
                    }
                    if (apm.sysMonChanged) {
                        if (apm.sysMonitoring) nextSysInfoSend = crtTime + apm.sysMonitorInterval * 1000; else nextSysInfoSend = -1;
                        apm.sysMonChanged = false;
                    }
                    if (apm.recheckChanged) {
                        if (apm.confCheck) nextRecheck = crtTime + apm.crtRecheckInterval * 1000; else nextRecheck = -1;
                        apm.recheckChanged = false;
                    }
                }
                if (haveChange) continue;
                timeRemained = nextOpTime - System.currentTimeMillis();
                haveTimeout = true;
                try {
                    if (timeRemained > 0) apm.mutexCond.wait(timeRemained);
                } catch (InterruptedException e) {
                }
                if (apm.condChanged) {
                    haveTimeout = false;
                }
                apm.condChanged = false;
            }
            crtTime = System.currentTimeMillis();
            if (haveTimeout) {
                if (nextOp == JOB_INFO_SEND) {
                    sendJobInfo();
                    nextJobInfoSend = crtTime + apm.getJobMonitorInterval() * 1000;
                }
                if (nextOp == SYS_INFO_SEND) {
                    sendSysInfo();
                    if (apm.getGenMonitoring()) {
                        if (generalInfoCount <= 1) sendGeneralInfo();
                        generalInfoCount = (generalInfoCount + 1) % apm.genMonitorIntervals;
                    }
                    nextSysInfoSend = crtTime + apm.getSysMonitorInterval() * 1000;
                }
                if (nextOp == RECHECK_CONF) {
                    Enumeration e = apm.confResources.keys();
                    boolean resourceChanged = false;
                    try {
                        while (e.hasMoreElements()) {
                            Object obj = e.nextElement();
                            Long lastModified = (Long) apm.confResources.get(obj);
                            if (obj instanceof File) {
                                File f = (File) obj;
                                logger.info(" [Checking for modifications for " + f.getCanonicalPath() + "]");
                                long lmt = f.lastModified();
                                if (lmt > lastModified.longValue()) {
                                    logger.info("[File " + f.getCanonicalPath() + " modified]");
                                    resourceChanged = true;
                                    break;
                                }
                            }
                            if (obj instanceof URL) {
                                URL u = (URL) obj;
                                long lmt = 0;
                                logger.info("[Checking for modifications for " + u + "]");
                                URLConnection urlConn = u.openConnection();
                                lmt = urlConn.getLastModified();
                                if (lmt > lastModified.longValue() || lmt == 0) {
                                    logger.info("[Location " + u + " modified]");
                                    resourceChanged = true;
                                    break;
                                }
                            }
                        }
                        if (resourceChanged) {
                            if (apm.initType == ApMon.FILE_INIT) {
                                apm.initialize((String) apm.initSource, false);
                            }
                            if (apm.initType == ApMon.LIST_INIT) {
                                apm.initialize((Vector) apm.initSource, false);
                            }
                        }
                        apm.setCrtRecheckInterval(apm.getRecheckInterval());
                    } catch (Throwable exc) {
                        apm.setCrtRecheckInterval(10 * apm.getRecheckInterval());
                    }
                    crtTime = System.currentTimeMillis();
                    nextRecheck = crtTime + apm.getRecheckInterval() * 1000;
                }
            }
        }
    }
