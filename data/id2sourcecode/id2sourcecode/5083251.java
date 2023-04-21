    public void release() {
        long diff = (REPORT_SLOW) ? VM_Time.cycles() - start : 0;
        boolean show = (verbose > 1) || (diff > SLOW_THRESHOLD);
        if (show) {
            Log.write("GC Warning: Thread ");
            writeThreadIdToLog(thread);
            Log.write(" released lock ");
            Log.write(id);
            Log.write(" ");
            Log.write(name);
            Log.write(" after ");
            Log.write(VM_Time.cyclesToMillis(diff));
            Log.writeln(" ms");
        }
        if (REPORT_SLOW) {
            endHistory[serving % 100] = VM_Time.cycles();
            setLocker(0, null, -1);
        }
        VM_Magic.sync();
        VM_Synchronization.fetchAndAdd(this, servingFieldOffset, 1);
    }
