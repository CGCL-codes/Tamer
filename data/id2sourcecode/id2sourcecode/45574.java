    void setupIterator(VM_CompiledMethod compiledMethod, int instructionOffset, int fp) {
        currentMethod = compiledMethod.getMethod();
        framePtr = fp;
        maps = ((VM_BaselineCompilerInfo) compiledMethod.getCompilerInfo()).referenceMaps;
        mapId = maps.locateGCPoint(instructionOffset, currentMethod);
        mapOffset = 0;
        if (mapId < 0) {
            VM_ReferenceMaps.jsrLock.lock();
            maps.setupJSRSubroutineMap(framePtr, mapId, compiledMethod);
        }
        if (VM.TraceStkMaps || TRACE_ALL) {
            VM.sysWrite("VM_BaselineGCMapIterator setupIterator mapId = ");
            VM.sysWrite(mapId);
            VM.sysWrite(".\n");
        }
        bridgeTarget = null;
        bridgeParameterTypes = null;
        bridgeParameterMappingRequired = false;
        bridgeRegistersLocationUpdated = false;
        bridgeParameterIndex = 0;
        bridgeRegisterIndex = 0;
        bridgeRegisterLocation = 0;
        bridgeSpilledParamLocation = 0;
        if (currentMethod.getDeclaringClass().isDynamicBridge()) {
            int ip = VM_Magic.getReturnAddress(fp);
            fp = VM_Magic.getCallerFramePointer(fp);
            int callingCompiledMethodId = VM_Magic.getCompiledMethodID(fp);
            VM_CompiledMethod callingCompiledMethod = VM_CompiledMethods.getCompiledMethod(callingCompiledMethodId);
            VM_CompilerInfo callingCompilerInfo = callingCompiledMethod.getCompilerInfo();
            int callingInstructionOffset = ip - VM_Magic.objectAsAddress(callingCompiledMethod.getInstructions());
            callingCompilerInfo.getDynamicLink(dynamicLink, callingInstructionOffset);
            bridgeTarget = dynamicLink.methodRef();
            bridgeParameterTypes = bridgeTarget.getParameterTypes();
            if (dynamicLink.isInvokedWithImplicitThisParameter()) {
                bridgeParameterInitialIndex = -1;
                bridgeSpilledParamInitialOffset = 8;
            } else {
                bridgeParameterInitialIndex = 0;
                bridgeSpilledParamInitialOffset = 4;
            }
            bridgeSpilledParamInitialOffset += (4 * bridgeTarget.getParameterWords());
            if (callingCompilerInfo.getCompilerType() == VM_CompilerInfo.BASELINE) {
                bridgeSpilledParameterMappingRequired = false;
            } else {
                bridgeSpilledParameterMappingRequired = true;
            }
        }
        reset();
    }
