    public void check(int w) {
        if (!REPORT_SLOW) return;
        if (VM.VerifyAssertions) VM._assert(VM_Thread.getCurrentThread() == thread);
        long diff = (REPORT_SLOW) ? VM_Time.cycles() - start : 0;
        boolean show = (verbose > 1) || (diff > SLOW_THRESHOLD);
        if (show) {
            Log.write("GC Warning: Thread ");
            writeThreadIdToLog(thread);
            Log.write(" reached point ");
            Log.write(w);
            Log.write(" while holding lock ");
            Log.write(id);
            Log.write(" ");
            Log.write(name);
            Log.write(" at ");
            Log.write(VM_Time.cyclesToMillis(diff));
            Log.writeln(" ms");
        }
        where = w;
    }
