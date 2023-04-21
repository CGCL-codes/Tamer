    public PetiteMultiClockPipe(int dataPathSize, ClockPin writeClock, ClockPin readClock) {
        if ((dataPathSize < 1) || (dataPathSize > 64)) {
            throw new net.sf.openforge.forge.api.ForgeApiException("Illegal data path size to PetiteMultiClockPipe: " + dataPathSize + ", expected 1 through 64");
        }
        if (writeClock == null) {
            writeClock = ClockDomain.GLOBAL.getClockPin();
        }
        if (readClock == null) {
            readClock = ClockDomain.GLOBAL.getClockPin();
        }
        moduleName = IPCORENAME + instanceCount;
        instanceCount++;
        asyncFifo = new IPCore(moduleName);
        asyncFifo.connectClock(readClock, "read_clock_in");
        asyncFifo.connectClock(writeClock, "write_clock_in");
        asyncFifo.connectReset(readClock.getResetPin(), "read_reset_in");
        asyncFifo.connectReset(writeClock.getResetPin(), "write_reset_in");
        asyncFifo.setWriter(this);
        writeEnable = new PinOut(asyncFifo, "write_enable_in", 1, 0);
        writeData = new PinOut(asyncFifo, "write_data_in", dataPathSize);
        fifoFull = new PinIn(asyncFifo, "full_out", 1);
        writeStatus = new PinIn(asyncFifo, "write_status_out", 2);
        readEnable = new PinOut(asyncFifo, "read_enable_in", 1, 0);
        readData = new PinIn(asyncFifo, "read_data_out", dataPathSize);
        fifoEmpty = new PinIn(asyncFifo, "empty_out", 1);
        readStatus = new PinIn(asyncFifo, "read_status_out", 2);
        this.bufferDepth = 15;
    }
