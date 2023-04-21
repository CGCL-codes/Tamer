    private static void insertStubCase(Assembler asm, int[] sigIds, RVMMethod[] targets, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        asm.resolveForwardReferences(bcIndices[middle]);
        if (low == middle && middle == high) {
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitJMP_Abs(Magic.getTocPointer().plus(target.getOffset()));
            } else {
                asm.emitJMP_RegDisp(ECX, target.getOffset());
            }
        } else {
            Offset disp = ArchEntrypoints.hiddenSignatureIdField.getOffset();
            ThreadLocalState.emitCompareFieldWithImm(asm, disp, sigIds[middle]);
            if (low < middle) {
                asm.emitJCC_Cond_Label(Assembler.LT, bcIndices[(low + middle - 1) / 2]);
            }
            if (middle < high) {
                asm.emitJCC_Cond_Label(Assembler.GT, bcIndices[(middle + 1 + high) / 2]);
            }
            RVMMethod target = targets[middle];
            if (target.isStatic()) {
                asm.emitJMP_Abs(Magic.getTocPointer().plus(target.getOffset()));
            } else {
                asm.emitJMP_RegDisp(ECX, target.getOffset());
            }
            if (low < middle) {
                insertStubCase(asm, sigIds, targets, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                insertStubCase(asm, sigIds, targets, bcIndices, middle + 1, high);
            }
        }
    }
