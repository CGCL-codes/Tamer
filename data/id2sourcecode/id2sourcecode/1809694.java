    public void visit(LegacyInstr.OUT i) {
        writeIORegister(i.imm1, readRegister(i.r1));
    }
