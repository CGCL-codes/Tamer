    public void visit(MSP430Instr.DADD i) {
        $write_poly_uint16(i.dest, performDeciAddCW($read_poly_uint16(i.source), $read_poly_uint16(i.dest), bit(C)));
    }
