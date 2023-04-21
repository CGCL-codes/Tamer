    public SSAInstruction copyForSSA(SSAInstructionFactory insts, int[] defs, int[] uses) {
        int newParams[] = params;
        Access[] reads = lexicalReads;
        if (uses != null) {
            int i = 0;
            newParams = new int[params.length];
            for (int j = 0; j < newParams.length; j++) newParams[j] = uses[i++];
            if (lexicalReads != null) {
                reads = new Access[lexicalReads.length];
                for (int j = 0; j < reads.length; j++) reads[j] = new Access(lexicalReads[j].variableName, lexicalReads[j].variableDefiner, uses[i++]);
            }
        }
        int newLvals[] = null;
        if (getNumberOfReturnValues() > 0) {
            newLvals = new int[results.length];
            System.arraycopy(results, 0, newLvals, 0, results.length);
        }
        int newExp = exception;
        Access[] writes = lexicalWrites;
        if (defs != null) {
            int i = 0;
            if (getNumberOfReturnValues() > 0) {
                newLvals[0] = defs[i++];
            }
            newExp = defs[i++];
            for (int j = 1; j < getNumberOfReturnValues(); j++) {
                newLvals[j] = defs[i++];
            }
            if (lexicalWrites != null) {
                writes = new Access[lexicalWrites.length];
                for (int j = 0; j < writes.length; j++) writes[j] = new Access(lexicalWrites[j].variableName, lexicalWrites[j].variableDefiner, defs[i++]);
            }
        }
        return copyInstruction(insts, newLvals, newParams, newExp, reads, writes);
    }
