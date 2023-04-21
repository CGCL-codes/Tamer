    TraceAction perform() throws MIPSInstructionException {
        long writeValue = owner.registers.readRegister(reg2) + owner.registers.readRegister(reg3);
        TraceAction a = owner.registers.writeRegister(reg1, writeValue);
        owner.incPC();
        return a;
    }
