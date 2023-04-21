    RaceCandidate(Invocation testCall, Trace trace, FieldInfo fi, Instruction readInsn, Instruction writeInsn) {
        this.testCall = testCall;
        this.fi = fi;
        this.readInsn = readInsn;
        this.writeInsn = writeInsn;
        this.trace = trace.clone();
    }
