    public void visit(LegacyInstr.IN i) {
        writeRegister(i.r1, readIORegister(i.imm1));
    }
