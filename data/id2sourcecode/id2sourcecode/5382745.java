    public static FlexotaskRunner cloneAndStartThread(FlexotaskSingleThreadRunner toClone, FlexotaskThreadFactory threadFactory, FlexotaskSchedulerRunnable[] otherRunnables) {
        int nRunnables = otherRunnables == null ? 1 : otherRunnables.length + 1;
        FlexotaskSchedulerRunnable[] runnables = new FlexotaskSchedulerRunnable[nRunnables];
        runnables[0] = toClone;
        if (nRunnables > 1) {
            System.arraycopy(otherRunnables, 0, runnables, 1, nRunnables - 1);
        }
        if (nRunnables > 1) {
            toClone.otherRunnables = new FlexotaskSchedulerRunnable[nRunnables - 1];
            for (int i = 0; i < nRunnables - 1; i++) {
                toClone.otherRunnables[i] = runnables[i + 1];
            }
            toClone.otherThreads = new Thread[nRunnables - 1];
        }
        Object[] theLocks = new Object[toClone.getThreadCoordinationLockCount() + 1];
        for (int i = 0; i < theLocks.length; i++) {
            theLocks[i] = new Object();
        }
        Thread[] allThreads = threadFactory.createThreads(runnables, theLocks);
        FlexotaskSingleThreadRunner clonedMasterRunnable = (FlexotaskSingleThreadRunner) runnables[0];
        if (nRunnables > 1) {
            for (int i = 0; i < nRunnables - 1; i++) {
                clonedMasterRunnable.otherThreads[i] = allThreads[i + 1];
            }
        }
        for (int i = 0; i < nRunnables; i++) {
            allThreads[i].start();
        }
        return new DelegatingFlexotaskRunner(clonedMasterRunnable);
    }
