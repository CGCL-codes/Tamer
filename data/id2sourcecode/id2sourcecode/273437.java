    static VM_MachineCode generateGlueCodeForNative(VM_CompiledMethod cm) {
        int compiledMethodId = cm.getId();
        VM_Method method = cm.getMethod();
        VM_Assembler asm = new VM_Assembler(100);
        int nativeIP = method.getNativeIP();
        int parameterWords = method.getParameterWords();
        prepareStackHeader(asm, method, compiledMethodId);
        storeParametersForLintel(asm, method);
        asm.emitMOV_Reg_Imm(S0, nativeIP);
        asm.emitCALL_RegDisp(JTOC, VM_Entrypoints.invokeNativeFunctionInstructionsField.getOffset());
        VM_ProcessorLocalState.emitMoveFieldToReg(asm, S0, VM_Entrypoints.activeThreadField.getOffset());
        asm.emitMOV_Reg_RegDisp(S0, S0, VM_Entrypoints.jniEnvField.getOffset());
        if (method.getReturnType().isReferenceType()) {
            asm.emitADD_Reg_RegDisp(T0, S0, VM_Entrypoints.JNIRefsField.getOffset());
            asm.emitMOV_Reg_RegInd(T0, T0);
        } else if (method.getReturnType().isLongType()) {
            asm.emitPUSH_Reg(T1);
        }
        VM_ProcessorLocalState.emitMoveFieldToReg(asm, S0, VM_Entrypoints.activeThreadField.getOffset());
        asm.emitMOV_Reg_RegDisp(S0, S0, VM_Entrypoints.jniEnvField.getOffset());
        popJNIrefForEpilog(asm);
        if (method.getReturnType().isLongType()) {
            asm.emitMOV_Reg_Reg(T1, T0);
            asm.emitPOP_Reg(T0);
        }
        VM_ProcessorLocalState.emitMoveFieldToReg(asm, S0, VM_Entrypoints.activeThreadField.getOffset());
        asm.emitMOV_Reg_RegDisp(S0, S0, VM_Entrypoints.jniEnvField.getOffset());
        asm.emitMOV_Reg_RegDisp(EBX, S0, VM_Entrypoints.JNIPendingExceptionField.getOffset());
        asm.emitMOV_RegDisp_Imm(S0, VM_Entrypoints.JNIPendingExceptionField.getOffset(), 0);
        asm.emitCMP_Reg_Imm(EBX, 0);
        VM_ForwardReference fr = asm.forwardJcc(asm.EQ);
        asm.emitMOV_Reg_Reg(T0, EBX);
        asm.emitMOV_Reg_RegDisp(T1, JTOC, VM_Entrypoints.athrowMethod.getOffset());
        asm.emitMOV_Reg_Reg(SP, EBP);
        asm.emitMOV_Reg_RegDisp(JTOC, SP, EDI_SAVE_OFFSET);
        asm.emitMOV_Reg_RegDisp(EBX, SP, EBX_SAVE_OFFSET);
        asm.emitMOV_Reg_RegDisp(EBP, SP, EBP_SAVE_OFFSET);
        asm.emitPOP_RegDisp(PR, VM_Entrypoints.framePointerField.getOffset());
        asm.emitJMP_Reg(T1);
        fr.resolve(asm);
        asm.emitMOV_Reg_Reg(SP, EBP);
        asm.emitMOV_Reg_RegDisp(JTOC, SP, EDI_SAVE_OFFSET);
        asm.emitMOV_Reg_RegDisp(EBX, SP, EBX_SAVE_OFFSET);
        asm.emitMOV_Reg_RegDisp(EBP, SP, EBP_SAVE_OFFSET);
        asm.emitPOP_RegDisp(PR, VM_Entrypoints.framePointerField.getOffset());
        if (method.isStatic()) asm.emitRET_Imm(parameterWords << LG_WORDSIZE); else asm.emitRET_Imm((parameterWords + 1) << LG_WORDSIZE);
        return new VM_MachineCode(asm.getMachineCodes(), null);
    }
