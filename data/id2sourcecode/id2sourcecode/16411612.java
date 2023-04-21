    private void shutdown() throws InterruptedException {
        if (stt != null) {
            stt.kill();
            stt.join();
        }
        if (mtt != null) {
            mtt.killAll();
            mtt.join();
        }
        if (writerThread != null) {
            System.out.println("Stop log writer thread... ");
            writerThread.interrupt();
            writerThread.join();
        }
        String[] bay = new String[11];
        bay[0] = "Stoped.";
        bay[1] = "See you soon";
        bay[2] = "Thank you for running and Goodbye...";
        bay[3] = "Bay...";
        bay[4] = "Terminated sir!";
        bay[5] = "Thanks for the use, Take care...";
        bay[6] = "GoodBay...";
        bay[7] = "Bay...";
        bay[8] = "Bay...";
        bay[9] = "Bay...";
        bay[10] = "Thank you for running, I'll miss you...";
        System.out.println(bay[new java.util.Random().nextInt(((Thread.currentThread().getName().equals("main") == true) ? 10 : 1))]);
    }
