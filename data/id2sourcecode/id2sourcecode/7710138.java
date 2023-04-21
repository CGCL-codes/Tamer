    private static void insertStubCase(VM_Assembler asm, int[] sigIds, VM_Method[] targets, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        asm.resolveForwardReferences(bcIndices[middle]);
        if (low == middle && middle == high) {
            VM_Method target = targets[middle];
            if (target.isStatic()) {
                VM_ProcessorLocalState.emitMoveFieldToReg(asm, ECX, VM_ArchEntrypoints.jtocField.getOffset());
            }
            asm.emitJMP_RegDisp(ECX, target.getOffset());
        } else {
            Offset disp = VM_ArchEntrypoints.hiddenSignatureIdField.getOffset();
            VM_ProcessorLocalState.emitCompareFieldWithImm(asm, disp, sigIds[middle]);
            if (low < middle) {
                asm.emitJCC_Cond_Label(VM_Assembler.LT, bcIndices[(low + middle - 1) / 2]);
            }
            if (middle < high) {
                asm.emitJCC_Cond_Label(VM_Assembler.GT, bcIndices[(middle + 1 + high) / 2]);
            }
            VM_Method target = targets[middle];
            if (target.isStatic()) {
                VM_ProcessorLocalState.emitMoveFieldToReg(asm, ECX, VM_ArchEntrypoints.jtocField.getOffset());
            }
            asm.emitJMP_RegDisp(ECX, target.getOffset());
            if (low < middle) {
                insertStubCase(asm, sigIds, targets, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                insertStubCase(asm, sigIds, targets, bcIndices, middle + 1, high);
            }
        }
    }
