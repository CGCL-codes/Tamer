    public ExecuteModbusCommand(ModbusTCPDevice device, int readOffset, int writeOffset, byte completionValue, byte[] command) {
        this.device = device;
        this.writeOffset = writeOffset;
        this.readOffset = readOffset;
        this.completionValue = completionValue;
        this.command = command;
    }
