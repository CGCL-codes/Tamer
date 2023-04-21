    private void pushFrameIP(ObjectReference code, int verbosity) {
        if (prevFp.isZero()) {
            if (verbosity >= 3) {
                Log.write(" t.contextRegisters.ip    = ");
                Log.writeln(thread.getContextRegisters().ip);
                Log.write("*t.contextRegisters.iploc = ");
                Log.writeln(thread.getContextRegisters().getIPLocation().loadAddress());
            }
            if (compiledMethodType != CompiledMethod.JNI) processCodeLocation(code, initialIPLoc); else if (verbosity >= 4) {
                Log.writeln("GC Warning: SKIPPING return address for JNI code");
            }
        } else {
            Address returnAddressLoc = Magic.getReturnAddressLocation(prevFp);
            Address returnAddress = returnAddressLoc.loadAddress();
            if (verbosity >= 4) {
                Log.write("--- Processing return address ");
                Log.write(returnAddress);
                Log.write(" located at ");
                Log.writeln(returnAddressLoc);
            }
            if (!DebugUtil.addrInBootImage(returnAddress)) processCodeLocation(code, returnAddressLoc);
        }
    }
