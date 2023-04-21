    private void transform(OPT_RegisterOperand rop) {
        OPT_Instruction inst = rop.instruction;
        switch(inst.getOpcode()) {
            case CALL_opcode:
                OPT_RegisterOperand invokee = Call.getParam(inst, 0).asRegister();
                if (invokee == rop) {
                    OPT_MethodOperand mop = Call.getMethod(inst);
                    if (mop.method.isSynchronized()) {
                        mop.spMethod = context.findOrCreateSpecializedVersion(mop.method);
                        if (DEBUG) VM.sysWrite("Identified call " + inst + " for unsynchronization\n");
                    }
                }
                break;
            case MONITORENTER_opcode:
                if (DEBUG) {
                    VM.sysWrite("Removing " + inst);
                }
                if ((!options.NO_CACHE_FLUSH)) {
                    inst.insertBefore(Empty.create(ISYNC));
                }
                OPT_DefUse.removeInstructionAndUpdateDU(inst);
                break;
            case MONITOREXIT_opcode:
                if (DEBUG) {
                    VM.sysWrite("Removing " + inst);
                }
                if ((!options.NO_CACHE_FLUSH)) {
                    inst.insertAfter(Empty.create(SYNC));
                }
                OPT_DefUse.removeInstructionAndUpdateDU(inst);
                break;
            default:
                break;
        }
    }
