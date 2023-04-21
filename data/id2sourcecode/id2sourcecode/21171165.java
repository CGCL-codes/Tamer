    public static void updateCommands(int base, int startReg, int endReg, int offsetReg, int stepReg) {
        Memory mem = getMemory();
        int start = getRegisterValue(startReg);
        int end = getRegisterValue(endReg);
        int offset = getRegisterValue(offsetReg);
        int step = getRegisterValue(stepReg);
        int skip = (step - 4) >> 2;
        IMemoryReader baseReader = MemoryReader.getMemoryReader(getRegisterValue(base), (end - start) << 4, 4);
        for (int i = start; i < end; i++) {
            baseReader.skip(1);
            int addr = baseReader.readNext();
            int count = baseReader.readNext();
            int dest = baseReader.readNext();
            IMemoryReader addrReader = MemoryReader.getMemoryReader(addr, count << 2, 4);
            IMemoryWriter destWriter = MemoryWriter.getMemoryWriter(dest + offset, count * step, 4);
            for (int j = 0; j < count; j++) {
                int src = addrReader.readNext();
                destWriter.writeNext(mem.read32(src));
                destWriter.skip(skip);
            }
            destWriter.flush();
        }
    }
