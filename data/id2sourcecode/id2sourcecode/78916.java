    void setupIterator(VM_CompiledMethod compiledMethod, int instructionOffset, VM_Address fp) {
        currentMethod = compiledMethod.getMethod();
        framePtr = fp;
        maps = ((VM_BaselineCompiledMethod) compiledMethod).referenceMaps;
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
        bridgeRegisterLocation = VM_Address.zero();
        bridgeSpilledParamLocation = VM_Address.zero();
        if (currentMethod.getDeclaringClass().isDynamicBridge()) {
            VM_Address ip = VM_Magic.getReturnAddress(fp);
            fp = VM_Magic.getCallerFramePointer(fp);
            int callingCompiledMethodId = VM_Magic.getCompiledMethodID(fp);
            VM_CompiledMethod callingCompiledMethod = VM_CompiledMethods.getCompiledMethod(callingCompiledMethodId);
            int callingInstructionOffset = ip.diff(VM_Magic.objectAsAddress(callingCompiledMethod.getInstructions()));
            callingCompiledMethod.getDynamicLink(dynamicLink, callingInstructionOffset);
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
            if (callingCompiledMethod.getCompilerType() == VM_CompiledMethod.BASELINE) {
                bridgeSpilledParameterMappingRequired = false;
            } else {
                bridgeSpilledParameterMappingRequired = true;
            }
        }
        reset();
    }
