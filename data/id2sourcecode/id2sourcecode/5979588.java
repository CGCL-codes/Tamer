    private void scanThreadInternal(Address gprs, int verbosity) {
        if (false) {
            VM.sysWriteln("Scanning thread ", thread.getThreadSlot(), " from thread ", RVMThread.getCurrentThreadSlot());
        }
        if (verbosity >= 2) {
            Log.writeln("--- Start Of Stack Scan ---\n");
            Log.write("Thread #");
            Log.writeln(thread.getThreadSlot());
        }
        if (VM.VerifyAssertions) assertImmovableInCurrentCollection();
        getHWExceptionRegisters();
        iteratorGroup.newStackWalk(thread, gprs);
        if (verbosity >= 2) dumpTopFrameInfo(verbosity);
        if (fp.NE(ArchitectureSpecific.StackframeLayoutConstants.STACKFRAME_SENTINEL_FP)) {
            prevFp = Address.zero();
            while (Magic.getCallerFramePointer(fp).NE(ArchitectureSpecific.StackframeLayoutConstants.STACKFRAME_SENTINEL_FP)) {
                if (false) {
                    VM.sysWriteln("Thread ", RVMThread.getCurrentThreadSlot(), " at fp = ", fp);
                }
                prevFp = scanFrame(verbosity);
                ip = Magic.getReturnAddress(fp);
                fp = Magic.getCallerFramePointer(fp);
            }
        }
        checkJNIBase();
        if (verbosity >= 2) Log.writeln("--- End Of Stack Scan ---\n");
    }
