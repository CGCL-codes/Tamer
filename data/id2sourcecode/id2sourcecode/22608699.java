    public HwBitField(String name, int width, int type, ArrayList<VerilogPort> readPorts, ArrayList<VerilogPort> writePorts, VerilogPort clkPort, VerilogPort resetPort) {
        super(name, width, type);
        this.readPorts = new ArrayList<VerilogPort>();
        this.readPorts.addAll(readPorts);
        this.writePorts = new ArrayList<VerilogPort>();
        this.writePorts.addAll(writePorts);
        this.clkPort = clkPort;
        this.resetPort = resetPort;
        if (0 == (type & READ_VAL_UNDEFINED)) {
            int portWidth = 0;
            for (VerilogPort p : readPorts) portWidth += p.getWidth();
            if (portWidth != width) throw new RuntimeException("Read port width mismatch: " + portWidth + ", expected: " + width);
        }
        if (0 == (type & WRITABLE)) {
            int portWidth = 0;
            for (VerilogPort p : readPorts) portWidth += p.getWidth();
            if (portWidth != width) throw new RuntimeException("Write port width mismatch: " + portWidth + ", expected: " + width);
            if (clkPort.getWidth() != 1) throw new RuntimeException("Clock port must be a single wire, actual size: " + clkPort.getWidth());
        }
    }
