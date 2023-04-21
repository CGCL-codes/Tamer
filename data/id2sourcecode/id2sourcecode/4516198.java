    protected String readWrite(int port, int mask) {
        return (ports[port].getPortMode() & mask) == IOPort.READ ? "read" : "write";
    }
