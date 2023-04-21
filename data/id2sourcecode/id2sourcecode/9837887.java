    private void pushFrameIP(ObjectReference code, int verbosity) {
        if (prevFp.isZero()) {
            if (verbosity >= 2) {
                Log.write(" t.contextRegisters.ip    = ");
                Log.writeln(thread.contextRegisters.ip);
                Log.write("*t.contextRegisters.iploc = ");
                Log.writeln(thread.contextRegisters.getIPLocation().loadAddress());
            }
            if (compiledMethodType != VM_CompiledMethod.JNI) {
                codeLocationsPush(code, initialIPLoc);
            } else if (verbosity >= 3) {
                Log.writeln("GC Warning: SKIPPING return address for JNI code");
            }
        } else {
            Address returnAddressLoc = VM_Magic.getReturnAddressLocation(prevFp);
            Address returnAddress = returnAddressLoc.loadAddress();
            if (verbosity >= 3) {
                Log.write("--- Processing return address ");
                Log.write(returnAddress);
                Log.write(" located at ");
                Log.writeln(returnAddressLoc);
            }
            if (!DebugUtil.addrInBootImage(returnAddress)) codeLocationsPush(code, returnAddressLoc);
        }
    }
