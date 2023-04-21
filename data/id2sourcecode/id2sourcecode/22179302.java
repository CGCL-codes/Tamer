    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("AsyncClientEchoTest client starting");
        connect();
        Thread writeThread = new Thread(new Writer());
        long starttime = JotTime.get();
        writeThread.start();
        int amt = 0;
        try {
            long thetime = JotTime.get();
            while (amt < (threadCount * messageCount) && starttime + 45 * 1000 > JotTime.get()) {
                amt = read();
                if (thetime + 10000 < JotTime.get()) {
                    System.out.println("read " + totalIn);
                    thetime = JotTime.get();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (amt < (threadCount * messageCount)) {
            System.out.println("FAILED ");
            for (int i = 0; i < threadCount; i++) {
                System.out.print("thread" + i);
                System.out.print(" outcounts" + outcounts[i]);
                System.out.print(" incounts" + incounts[i]);
                System.out.println();
            }
        }
        long endtime = JotTime.get();
        System.out.println("reading finished");
        close();
        long totalms = (endtime - starttime);
        double totalseconds = (double) totalms / 1000;
        System.out.println("test is done");
        System.out.println("total time is " + totalms + " ms " + "for " + threadCount * messageCount + " reads of " + messageSize + " each");
        System.out.println("or " + (threadCount * messageCount) / totalseconds + " reads/sec");
        System.out.println("data rate is " + ((long) (threadCount * messageCount) * messageSize) / totalseconds + " bytes/sec");
        System.out.println("data rate is " + ((long) (threadCount * messageCount) * messageSize / (1024 * 1024)) / totalseconds + " M bytes/sec");
    }
