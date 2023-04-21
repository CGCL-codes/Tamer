    private static void printResult() throws InterruptedException {
        readExec.shutdown();
        writeExec.shutdown();
        System.out.println("readThreadCount:" + readThreadCount);
        System.out.println("writeThreadCount:" + writeThreadCount);
        System.out.println();
        readExec.awaitTermination(waitSecond, TimeUnit.SECONDS);
        if (readExec.isShutdown()) {
            System.out.println("readCount:" + readCount);
        } else {
            System.out.println("readExec not shutDown");
        }
        System.out.println("totalReadTime:" + totalReadTime);
        double avgTimePerReadThread = (double) totalReadTime.longValue() / readThreadCount;
        System.out.println("avgTimePerReadThread:" + avgTimePerReadThread);
        System.out.println("readPerSecond:" + readCount / (avgTimePerReadThread / 1000));
        System.out.println("==============================");
        writeExec.awaitTermination(waitSecond, TimeUnit.SECONDS);
        if (writeExec.isShutdown()) {
            System.out.println("writeCount:" + writeCount);
        } else {
            System.out.println("writeExec not shutDown");
        }
        System.out.println("totalWriteTime:" + totalWriteTime);
        double avgTimePerWriteThread = (double) totalWriteTime.longValue() / writeThreadCount;
        System.out.println("avgTimePerWriteThread:" + avgTimePerWriteThread);
        System.out.println("writePerSecond:" + writeCount / (avgTimePerWriteThread / 1000));
        System.out.println("==============================");
        System.out.println("cache size:" + cache.size());
    }
