    public CrawlThread(Queue node, Queue write, Queue hosts, int t, int n) {
        nodeQueue = node;
        writeQueue = write;
        allHosts = hosts;
        timeout = t;
        name = n;
    }
