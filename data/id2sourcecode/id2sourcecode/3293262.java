    public void visit(LegacyInstr.CALL i) {
        int tmp_0 = (pc + 4) / 2;
        pushByte(low(tmp_0));
        pushByte(high(tmp_0));
        nextPC = i.imm1 * 2;
        cyclesConsumed += 4;
    }
