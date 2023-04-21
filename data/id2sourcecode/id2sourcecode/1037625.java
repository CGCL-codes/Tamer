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
                    VM.sysWrite("GC Warning: slow/deadlock - thread ");
                    VM_Thread.getCurrentThread().dump(1);
                    VM.sysWrite(" with ticket ", ticket);
                    VM.sysWrite(" failed to acquire lock ", id);
                    VM.sysWrite(" (", name);
                    VM.sysWriteln(") serving ", serving);
                    VM.sysWriteln(" after ", VM_Time.cyclesToMillis(waitTime), " ms");
                    VM_Thread t = thread;
                    if (t == null) VM.sysWriteln("GC Warning: Locking thread unknown"); else {
                        VM.sysWrite("GC Warning:  Locking thread: ");
                        t.dump(1);
                        VM.sysWriteln(" at position ", where);
                    }
                    VM.sysWriteln("*** my start = ", localStart);
                    for (int i = (serving - 10) % 100; i <= (serving % 100); i++) {
                        VM.sysWrite(i, ": index ", servingHistory[i]);
                        VM.sysWrite("   tid ", tidHistory[i]);
                        VM.sysWrite("    start = ", startHistory[i]);
                        VM.sysWrite("    end = ", endHistory[i]);
                        VM.sysWriteln("    start-myStart = ", VM_Time.cyclesToMillis(startHistory[i] - localStart));
                    }
                }
                if (waitTime > TIME_OUT) {
                    VM.sysWrite("GC Warning: Locked out thread: ");
                    VM_Thread.getCurrentThread().dump(1);
                    VM_Scheduler.dumpStack();
                    VM_Interface.sysFail("Deadlock or someone holding on to lock for too long");
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
            VM.sysWrite("Thread ");
            thread.dump();
            VM.sysWrite(" acquired lock ", id);
            VM.sysWriteln(" ", name);
        }
        VM_Magic.isync();
    }
