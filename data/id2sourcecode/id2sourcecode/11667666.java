    public void visit(AVRInstr.SBR i) {
        $write_int8(i.rd, performOr($read_int8(i.rd), i.imm.value));
    }
