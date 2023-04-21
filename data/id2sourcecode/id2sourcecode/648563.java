    private void startThread() {
        Runnable r = new Runnable() {

            public void run() {
                runWork();
            }
        };
        runit = true;
        timerThread = new Thread(r, "Timer");
        timerThread.start();
        writeMessage("Started thread.");
    }
