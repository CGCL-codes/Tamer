    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("AsyncBlockData client starting");
        connect();
        Thread writeThread = new Thread(new Writer());
        long starttime = JotTime.get();
        writeThread.start();
        int amt = 0;
        while (amt < (threadCount * fetchCount)) {
            amt = read();
        }
        long endtime = JotTime.get();
        System.out.println("reading finished");
        close();
        long totalms = (endtime - starttime);
        double totalseconds = (double) totalms / 1000;
        System.out.println("test is done");
        System.out.println("total time is " + totalms + " ms " + "for " + threadCount * fetchCount + " reads of " + BlockDataServer.size + " each");
        System.out.println("or " + (threadCount * fetchCount) / totalseconds + " reads/sec");
        System.out.println("data rate is " + ((long) (threadCount * fetchCount) * BlockDataServer.size) / totalseconds + " bytes/sec");
        System.out.println("data rate is " + ((long) (threadCount * fetchCount) * BlockDataServer.size / (1024 * 1024)) / totalseconds + " M bytes/sec");
    }
