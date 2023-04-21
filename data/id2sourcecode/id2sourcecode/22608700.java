    public HwBitField(String name, int width, int type, VerilogPort readPort, VerilogPort writePort, VerilogPort clkPort, VerilogPort resetPort) {
        super(name, width, type);
        this.readPorts = new ArrayList<VerilogPort>();
        this.writePorts = new ArrayList<VerilogPort>();
        if (null != readPort) readPorts.add(readPort);
        if (null != writePort) writePorts.add(writePort);
        this.clkPort = clkPort;
        this.resetPort = resetPort;
        if (0 == (type & READ_VAL_UNDEFINED)) {
            if (readPort.getWidth() != width) throw new RuntimeException("Read port width mismatch: " + readPort.getWidth() + ", expected: " + width);
        }
        if (0 != (type & WRITABLE)) {
            if (writePort.getWidth() != width) throw new RuntimeException("Write port width mismatch: " + writePort.getWidth() + ", expected: " + width);
            if (clkPort.getWidth() != 1) throw new RuntimeException("Clock port must be a single wire, actual size: " + clkPort.getWidth());
        }
    }
