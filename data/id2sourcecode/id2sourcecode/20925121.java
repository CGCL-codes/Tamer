    public void scatterGather() throws Exception {
        Thread feederThread = new Thread(new Runnable() {

            public void run() {
                try {
                    Feeder feeder = new Feeder();
                    for (; ; ) {
                        if (shouldTerminate) {
                            break;
                        }
                        if (numEntriesSent > MAX_ENTRIES_TO_FEED) {
                            break;
                        }
                        if (isSpaceAboveHighWaterMark()) {
                            for (; ; ) {
                                log.info("Space full, pausing");
                                pause(RETRY_INTERVAL_MILLIS);
                                if (isSpaceBelowLowWaterMark()) {
                                    break;
                                }
                            }
                        }
                        try {
                            feeder.process();
                        } catch (Exception e) {
                            log.warn("Feeder process error, retrying...", e);
                            pause(RETRY_INTERVAL_MILLIS);
                            continue;
                        }
                        numEntriesSent++;
                    }
                } catch (Exception e) {
                    log.error("Exception creating feeder process", e);
                }
            }
        });
        Thread writerThread = new Thread(new Runnable() {

            public void run() {
                try {
                    WriteProcessor writer = new WriteProcessor();
                    for (; ; ) {
                        if (shouldTerminate) {
                            break;
                        }
                        try {
                            writer.process();
                        } catch (Exception e) {
                            log.warn("Error processing write, retrying...", e);
                            pause(RETRY_INTERVAL_MILLIS);
                            continue;
                        }
                        numEntriesReceived++;
                    }
                    writer.destroy();
                } catch (Exception e) {
                    log.error("Exception creating collector process", e);
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                shouldTerminate = true;
            }
        }));
        feederThread.start();
        writerThread.start();
        feederThread.join();
        writerThread.join();
    }
