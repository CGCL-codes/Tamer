    public void visit(LegacyInstr.ICALL i) {
        int tmp_0 = (pc + 2) / 2;
        pushByte(low(tmp_0));
        pushByte(high(tmp_0));
        nextPC = getRegisterWord(RZ) * 2;
        cyclesConsumed += 3;
    }
