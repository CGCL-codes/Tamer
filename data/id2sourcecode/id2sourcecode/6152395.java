    public PerfMonWorker() throws IOException {
        acceptSelector = Selector.open();
        sendSelector = Selector.open();
        writerThread = new Thread(this);
        sigar = SigarProxyCache.newInstance(new Sigar(), 500);
    }
