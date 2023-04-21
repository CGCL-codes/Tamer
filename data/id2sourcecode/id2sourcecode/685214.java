    public void run() {
        System.out.println("Starting " + numThreads + " Threads...");
        for (int i = 0; i < numThreads; i++) {
            threads.add(new CrawlThread(nodeQueue, writeQueue, allHosts, timeout, i));
        }
        for (int i = 0; i < numThreads; i++) {
            threads.get(i).start();
        }
        CrawlResult crawlResult;
        while (true) {
            try {
                crawlResult = (CrawlResult) writeQueue.dequeue();
                crawlWriter.write(crawlResult);
            } catch (InterruptedException e) {
                for (int i = 0; i < numThreads; i++) {
                    threads.get(i).interrupt();
                }
                crawlWriter.close();
                return;
            }
        }
    }
