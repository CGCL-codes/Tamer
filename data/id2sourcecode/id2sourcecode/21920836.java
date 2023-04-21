    protected short z_call() {
        int nlocals;
        int i;
        if (operands[0] == 0) {
            if (isstore()) zm.set_variable(storevar, ZFALSE);
        } else {
            zm.zstack.push(new ZFrameBound(isstore()));
            if (isstore()) zm.zstack.push(new Integer(storevar));
            zm.zstack.push(new Integer(opnum));
            zm.zstack.push(new Integer(zm.pc));
            zm.zstack.push(new Integer(((ZMachine5) zm).argcount));
            zm.zstack.push(zm.locals);
            zm.pc = zm.routine_address(operands[0]);
            nlocals = zm.get_code_byte();
            ((ZMachine5) zm).argcount = (short) (count - 1);
            zm.locals = new short[nlocals];
            for (i = 0; i < nlocals; i++) {
                if (i < (count - 1)) {
                    zm.locals[i] = operands[i + 1];
                } else {
                    zm.locals[i] = 0;
                }
            }
        }
        return ZFALSE;
    }
