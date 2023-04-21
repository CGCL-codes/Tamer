    private StructuralMemoryPort createInterface(LogicalMemoryPort memPort, int memWidth) {
        final Port address = makeDataPort();
        address.setSize(getAddrWidth(), false);
        address.getPeer().setSize(getAddrWidth(), false);
        final Port size = makeDataPort();
        size.setSize(LogicalMemory.SIZE_WIDTH, false);
        size.getPeer().setSize(LogicalMemory.SIZE_WIDTH, false);
        Port en = null;
        Port wen = null;
        Port din = null;
        Bus dout = null;
        boolean read = false;
        boolean write = false;
        if (!memPort.isWriteOnly()) {
            read = true;
            en = makeDataPort();
            en.setSize(1, false);
            en.getPeer().setSize(1, false);
            dout = getExit(Exit.DONE).makeDataBus();
            dout.setSize(memWidth, true);
        }
        if (!memPort.isReadOnly()) {
            write = true;
            wen = makeDataPort();
            wen.setSize(1, false);
            din = makeDataPort();
            din.setSize(memWidth, true);
        }
        final Bus done = getExit(Exit.DONE).makeDataBus();
        done.setSize(1, true);
        StructuralMemoryPort port = new StructuralMemoryPort(address, din, en, wen, size, dout, done, read, write);
        this.memPorts.put(memPort, port);
        return port;
    }
