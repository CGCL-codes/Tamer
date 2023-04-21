    public void visit(MSP430Instr.DADD_B i) {
        $write_poly_int8(i.dest, performDeciAddC($read_poly_int8(i.source), $read_poly_int8(i.dest), bit(C)));
    }
