    public NioServer(int permanentAcceptThreads, int permanentTransferThreads) throws IOException {
        if (permanentAcceptThreads < 1) throw new IllegalArgumentException("permanentAcceptThreads < 1");
        if (permanentTransferThreads < 1) throw new IllegalArgumentException("permanentTransferThreads < 1");
        acceptSelectors = new SelectorPool(new AcceptSelectorLoop(), this, permanentAcceptThreads);
        if (fullDuplexTransfer) {
            readSelectors = new SelectorPool(new ReadSelectorLoop(), this, permanentTransferThreads);
            writeSelectors = new SelectorPool(new WriteSelectorLoop(), this, permanentTransferThreads);
        } else {
            readSelectors = new SelectorPool(new ReadWriteSelectorLoop(), this, permanentTransferThreads);
            writeSelectors = null;
        }
        connectSelectors = new SelectorPool(new ConnectSelectorLoop(this), this, permanentAcceptThreads);
    }
