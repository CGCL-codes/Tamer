    public HwBitField(HwBitField bf, VerilogPort readPort, VerilogPort writePort) {
        super(bf.name, bf.width + 1, bf.type);
        this.readPorts = new ArrayList<VerilogPort>();
        this.writePorts = new ArrayList<VerilogPort>();
        readPorts.addAll(bf.readPorts);
        if (null != readPort) readPorts.add(readPort);
        writePorts.addAll(bf.writePorts);
        if (null != writePort) writePorts.add(writePort);
        this.clkPort = bf.clkPort;
        this.resetPort = bf.resetPort;
    }
