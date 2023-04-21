    @Unpreemptible
    public void run() {
        while (true) {
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Waiting for request...]");
            waitForRequest();
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Request recieved.]");
            long startTime = VM.statistics.nanoTime();
            if (concurrentCollection) {
                if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Stopping concurrent collectors...]");
                Plan.concurrentWorkers.abortCycle();
                Plan.concurrentWorkers.waitForCycle();
                Phase.clearConcurrentPhase();
                concurrentCollection = false;
            }
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Stopping the world...]");
            VM.collection.stopAllMutators();
            boolean userTriggeredCollection = Plan.isUserTriggeredCollection();
            boolean internalTriggeredCollection = Plan.isInternalTriggeredCollection();
            clearRequest();
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Triggering worker threads...]");
            workers.triggerCycle();
            workers.waitForCycle();
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Worker threads complete!]");
            long elapsedTime = VM.statistics.nanoTime() - startTime;
            HeapGrowthManager.recordGCTime(VM.statistics.nanosToMillis(elapsedTime));
            if (VM.activePlan.global().lastCollectionFullHeap() && !internalTriggeredCollection) {
                if (Options.variableSizeHeap.getValue() && !userTriggeredCollection) {
                    if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Considering heap size.]");
                    HeapGrowthManager.considerHeapSize();
                }
                HeapGrowthManager.reset();
            }
            Plan.resetCollectionTrigger();
            if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Resuming mutators...]");
            VM.collection.resumeAllMutators();
            if (concurrentCollection) {
                if (Options.verbose.getValue() >= 5) Log.writeln("[STWController: Triggering concurrent collectors...]");
                Plan.concurrentWorkers.triggerCycle();
            }
        }
    }
