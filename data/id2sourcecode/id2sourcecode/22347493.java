    public void visit(MSP430Instr.BIS i) {
        $write_poly_uint16(i.dest, $read_poly_uint16(i.source) | $read_poly_uint16(i.dest));
    }
