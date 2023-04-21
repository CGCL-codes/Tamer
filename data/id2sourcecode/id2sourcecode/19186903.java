    public static void trace(String who, String what) {
        lockOutput();
        VM_Processor.getCurrentProcessor().disableThreadSwitching();
        writeDecimal(VM_Processor.getCurrentProcessorId());
        writeString("[");
        VM_Thread t = VM_Thread.getCurrentThread();
        t.dump();
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
        writeString("\n");
        VM_Processor.getCurrentProcessor().enableThreadSwitching();
        unlockOutput();
    }
