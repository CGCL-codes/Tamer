    private void dumpTopFrameInfo(int verbosity) {
        Log.write("   topFrame = ");
        Log.writeln(topFrame);
        Log.write("         ip = ");
        Log.writeln(ip);
        Log.write("         fp = ");
        Log.writeln(fp);
        Log.write("  registers.ip = ");
        Log.writeln(thread.getContextRegisters().ip);
        if (verbosity >= 3 && thread.getJNIEnv() != null) thread.getJNIEnv().dumpJniRefsStack();
    }
