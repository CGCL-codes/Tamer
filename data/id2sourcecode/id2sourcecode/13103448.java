    TraceAction perform() throws MIPSInstructionException {
        int writeValue = owner.registers.readRegister(reg2) < owner.registers.readRegister(reg3) ? 1 : 0;
        TraceAction a = owner.registers.writeRegister(reg1, writeValue);
        owner.incPC();
        return a;
    }
