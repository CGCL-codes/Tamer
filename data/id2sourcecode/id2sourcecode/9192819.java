    @Override
    protected final void startProcess() {
        engineThread = new Thread(new Runnable() {

            public void run() {
                mainLoop(guiToEngine, engineToGui);
            }
        });
        int pMin = Thread.MIN_PRIORITY;
        int pNorm = Thread.NORM_PRIORITY;
        int prio = pMin + (pNorm - pMin) / 2;
        engineThread.setPriority(prio);
        engineThread.start();
    }
