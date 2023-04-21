    public void visit(LegacyInstr.RCALL i) {
        int tmp_0 = (pc + 2) / 2;
        pushByte(low(tmp_0));
        pushByte(high(tmp_0));
        nextPC = (i.imm1 + tmp_0) * 2;
        cyclesConsumed += 3;
    }
