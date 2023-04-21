    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            usage();
        }
        int writeWaitTime = 0;
        try {
            writeWaitTime = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            usage();
        }
        final NewsReceiver receiver = new NewsReceiver(new File(args[0]), writeWaitTime);
        final int writeWaitTimeCopy = writeWaitTime;
        Thread updateThread = new Thread() {

            public void run() {
                super.run();
                while (true) {
                    receiver.update();
                    try {
                        Thread.sleep(writeWaitTimeCopy);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        updateThread.setDaemon(true);
        updateThread.start();
    }
