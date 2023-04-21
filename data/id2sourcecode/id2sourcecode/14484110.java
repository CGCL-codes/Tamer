    TraceAction perform() throws MIPSInstructionException {
        long writeValue = owner.registers.readRegister(reg2) | constant;
        TraceAction a = owner.registers.writeRegister(reg1, writeValue);
        owner.incPC();
        return a;
    }
