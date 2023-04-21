    private static void _trace(String who, String what, int howmany, boolean hex) {
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
        if (hex) writeHex(howmany); else writeDecimal(howmany);
        writeString("\n");
        unlockOutput();
        VM_Processor.getCurrentProcessor().enableThreadSwitching();
    }
