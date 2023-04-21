    public void acquire() {
        int ticket = VM_Synchronization.fetchAndAdd(this, dispenserFieldOffset, 1);
        int retryCountdown = TIMEOUT_CHECK_FREQ;
        long localStart = 0;
        long lastSlowReport = 0;
        while (ticket != serving) {
            if (localStart == 0) lastSlowReport = localStart = VM_Time.cycles();
            if (--retryCountdown == 0) {
                retryCountdown = TIMEOUT_CHECK_FREQ;
                long now = VM_Time.cycles();
                long lastReportDuration = now - lastSlowReport;
                long waitTime = now - localStart;
                if (lastReportDuration > SLOW_THRESHOLD + VM_Time.millisToCycles(200 * (VM_Thread.getCurrentThread().getIndex() % 5))) {
                    lastSlowReport = now;
                    Log.write("GC Warning: slow/deadlock - thread ");
                    writeThreadIdToLog(VM_Thread.getCurrentThread());
                    Log.write(" with ticket ");
                    Log.write(ticket);
                    Log.write(" failed to acquire lock ");
                    Log.write(id);
                    Log.write(" (");
                    Log.write(name);
                    Log.write(") serving ");
                    Log.write(serving);
                    Log.write(" after ");
                    Log.write(VM_Time.cyclesToMillis(waitTime));
                    Log.write(" ms");
                    Log.writelnNoFlush();
                    VM_Thread t = thread;
                    if (t == null) Log.writeln("GC Warning: Locking thread unknown", false); else {
                        Log.write("GC Warning: Locking thread: ");
                        writeThreadIdToLog(t);
                        Log.write(" at position ");
                        Log.writeln(where, false);
                    }
                    Log.write("GC Warning: my start = ");
                    Log.writeln(localStart, false);
                    for (int i = (serving + 90) % 100; i != (serving % 100); i = (i + 1) % 100) {
                        if (VM.VerifyAssertions) VM._assert(i >= 0 && i < 100);
                        Log.write("GC Warning: ");
                        Log.write(i);
                        Log.write(": index ");
                        Log.write(servingHistory[i]);
                        Log.write("   tid ");
                        Log.write(tidHistory[i]);
                        Log.write("    start = ");
                        Log.write(startHistory[i]);
                        Log.write("    end = ");
                        Log.write(endHistory[i]);
                        Log.write("    start-myStart = ");
                        Log.write(VM_Time.cyclesToMillis(startHistory[i] - localStart));
                        Log.writelnNoFlush();
                    }
                    Log.flush();
                }
                if (waitTime > TIME_OUT) {
                    Log.write("GC Warning: Locked out thread: ");
                    writeThreadIdToLog(VM_Thread.getCurrentThread());
                    Log.writeln();
                    VM_Scheduler.dumpStack();
                    VM.sysFail("Deadlock or someone holding on to lock for too long");
                }
            }
        }
        if (REPORT_SLOW) {
            servingHistory[serving % 100] = serving;
            tidHistory[serving % 100] = VM_Thread.getCurrentThread().getIndex();
            startHistory[serving % 100] = VM_Time.cycles();
            setLocker(VM_Time.cycles(), VM_Thread.getCurrentThread(), -1);
        }
        if (verbose > 1) {
            Log.write("Thread ");
            writeThreadIdToLog(thread);
            Log.write(" acquired lock ");
            Log.write(id);
            Log.write(" ");
            Log.write(name);
            Log.writeln();
        }
        VM_Magic.isync();
    }
