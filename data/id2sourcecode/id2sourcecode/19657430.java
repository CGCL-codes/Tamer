    private void init(int readThreadsCount, int writeThreadsCount, int readTries, int writeTries) throws IOException {
        initReaders(readThreadsCount, readTries);
        initWriters(writeThreadsCount, writeTries);
        if (readers.size() == 0 || writers.size() == 0) {
            log.error("Error in threads initialization !");
            System.exit(1);
        }
        selector = SelectorProvider.provider().openSelector();
        key = ssc.register(selector, SelectionKey.OP_ACCEPT, this);
    }
