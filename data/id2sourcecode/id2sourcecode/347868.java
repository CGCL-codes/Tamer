    private static boolean synchronizesOn(OPT_IR ir, OPT_Register r) {
        for (OPT_RegisterOperand use = r.useList; use != null; use = (OPT_RegisterOperand) use.getNext()) {
            OPT_Instruction s = use.instruction;
            if (s.operator == MONITORENTER) return true;
            if (s.operator == MONITOREXIT) return true;
            if (Call.conforms(s)) {
                VM_Method m = Call.getMethod(s).method;
                if (!m.isStatic()) {
                    OPT_RegisterOperand invokee = Call.getParam(s, 0).asRegister();
                    if (invokee == use) {
                        if (m.isSynchronized()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
