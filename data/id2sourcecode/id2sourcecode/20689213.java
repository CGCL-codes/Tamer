    public BankPort createPort(boolean read, boolean write, String writeMode) {
        Port a = makeDataPort();
        Port d = makeDataPort();
        Port e = makeDataPort();
        Port w = makeDataPort();
        Bus b = getExit(Exit.DONE).makeDataBus();
        if (this.ports == Collections.EMPTY_LIST) this.ports = new ArrayList(2);
        BankPort port = createBankPort(a, d, e, w, b, read, write, writeMode);
        this.ports.add(port);
        return port;
    }
