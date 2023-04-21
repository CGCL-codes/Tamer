    private void dumpTopFrameInfo(int verbosity) {
        Log.write("   topFrame = ");
        Log.writeln(topFrame);
        Log.write("         ip = ");
        Log.writeln(ip);
        Log.write("         fp = ");
        Log.writeln(fp);
        Log.write("  registers.ip = ");
        Log.writeln(thread.contextRegisters.ip);
        if (verbosity >= 2 && thread.jniEnv != null) thread.jniEnv.dumpJniRefsStack();
    }
