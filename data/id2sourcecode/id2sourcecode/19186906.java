    public static void trace(String who, String what, VM_Address addr) {
        VM_Processor.getCurrentProcessor().disableThreadSwitching();
        lockOutput();
        writeDecimal(VM_Processor.getCurrentProcessorId());
        writeString("[");
        VM_Thread.getCurrentThread().dump();
        writeString("] ");
        if (traceDetails) {
            writeString("(");
            writeDecimal(numDaemons);
            writeString("/");
            writeDecimal(numActiveThreads);
            writeString(") ");
        }
        writeString(who);
        writeString(": ");
        writeString(what);
        writeString(" ");
        writeHex(addr);
        writeString("\n");
        unlockOutput();
        VM_Processor.getCurrentProcessor().enableThreadSwitching();
    }
