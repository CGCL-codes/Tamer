    private static void insertStubCase(Assembler asm, int[] sigIds, RVMMethod[] targets, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        asm.resolveForwardReferences(bcIndices[middle]);
        if (low == middle && middle == high) {
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitLAddrToc(S0, target.getOffset());
            } else {
                asm.emitLAddrOffset(S0, S0, target.getOffset());
            }
            asm.emitMTCTR(S0);
            asm.emitBCCTR();
        } else {
            asm.emitCMPI(S1, sigIds[middle]);
            if (low < middle) {
                asm.emitShortBC(LT, 0, bcIndices[(low + middle - 1) / 2]);
            }
            if (middle < high) {
                asm.emitShortBC(GT, 0, bcIndices[(middle + 1 + high) / 2]);
            }
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitLAddrToc(S0, target.getOffset());
            } else {
                asm.emitLAddrOffset(S0, S0, target.getOffset());
            }
            asm.emitMTCTR(S0);
            asm.emitBCCTR();
            if (low < middle) {
                insertStubCase(asm, sigIds, targets, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                insertStubCase(asm, sigIds, targets, bcIndices, middle + 1, high);
            }
        }
    }
