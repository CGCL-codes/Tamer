    protected short op_call() {
        int nlocals;
        int i;
        short thislocal;
        if (operands[0] == 0) {
            zm.set_variable(storevar, ZFALSE);
        } else {
            zm.zstack.push(new ZFrameBound(isstore()));
            zm.zstack.push(new Integer(storevar));
            zm.zstack.push(new Integer(zm.pc));
            zm.zstack.push(zm.locals);
            zm.pc = zm.routine_address(operands[0]);
            nlocals = zm.get_code_byte();
            zm.locals = new short[nlocals];
            for (i = 0; i < nlocals; i++) {
                thislocal = (short) (((zm.get_code_byte() << 8) & 0xFF00) | (zm.get_code_byte() & 0xFF));
                if (i < (count - 1)) {
                    zm.locals[i] = operands[i + 1];
                } else {
                    zm.locals[i] = thislocal;
                }
            }
        }
        return ZFALSE;
    }
