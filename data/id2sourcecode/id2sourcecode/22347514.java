    public void visit(MSP430Instr.MOV i) {
        $write_poly_uint16(i.dest, $read_poly_uint16(i.source));
    }
