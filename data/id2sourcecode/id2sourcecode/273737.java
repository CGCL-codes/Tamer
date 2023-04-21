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
        if (VM.TraceStkMaps) {
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
        if (currentMethod.getDeclaringClass().isDynamicBridge()) {
            fp = VM_Magic.getCallerFramePointer(fp);
            int ip = VM_Magic.getNextInstructionAddress(fp);
            int callingCompiledMethodId = VM_Magic.getCompiledMethodID(fp);
            VM_CompiledMethod callingCompiledMethod = VM_ClassLoader.getCompiledMethod(callingCompiledMethodId);
            VM_CompilerInfo callingCompilerInfo = callingCompiledMethod.getCompilerInfo();
            int callingInstructionOffset = ip - VM_Magic.objectAsAddress(callingCompiledMethod.getInstructions());
            callingCompilerInfo.getDynamicLink(dynamicLink, callingInstructionOffset);
            bridgeTarget = dynamicLink.methodRef();
            bridgeParameterInitialIndex = dynamicLink.isInvokedWithImplicitThisParameter() ? -1 : 0;
            bridgeParameterTypes = bridgeTarget.getParameterTypes();
        }
        reset();
    }
