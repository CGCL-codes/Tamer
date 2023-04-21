    public void run() {
        this.pid = SystemInterface.init_thread();
        Unsafe.setThreadBlock(this.myself);
        SystemInterface.set_thread_priority(this.tid, SystemInterface.THREAD_PRIORITY_TIME_CRITICAL);
        for (; ; ) {
            SystemInterface.msleep(QUANTA);
            other_nt.suspend();
            jq_Thread javaThread = other_nt.getCurrentJavaThread();
            if (javaThread.isThreadSwitchEnabled()) {
                if (STATISTICS) ++enabledCount;
                if (TRACE) SystemInterface.debugwriteln("TICK! " + other_nt + " Java Thread = " + javaThread);
                javaThread.disableThreadSwitch();
                Assert._assert(other_nt.getCurrentJavaThread() == javaThread);
                jq_RegisterState regs = javaThread.getRegisterState();
                regs.setContextFlags(jq_RegisterState.CONTEXT_CONTROL | jq_RegisterState.CONTEXT_INTEGER | jq_RegisterState.CONTEXT_FLOATING_POINT);
                boolean b = other_nt.getContext(regs);
                if (!b) {
                    if (TRACE) SystemInterface.debugwriteln("Failed to get thread context for " + other_nt);
                } else {
                    if (TRACE) SystemInterface.debugwriteln(other_nt + " : " + javaThread + " ip=" + regs.getEip().stringRep() + " sp=" + regs.getEsp().stringRep() + " cc=" + CodeAllocator.getCodeContaining(regs.getEip()));
                    regs.setEsp((StackAddress) regs.getEsp().offset(-HeapAddress.size()));
                    regs.getEsp().poke(HeapAddress.addressOf(other_nt));
                    regs.setEsp((StackAddress) regs.getEsp().offset(-CodeAddress.size()));
                    regs.getEsp().poke(regs.getEip());
                    regs.setEip(jq_NativeThread._threadSwitch.getDefaultCompiledVersion().getEntrypoint());
                    regs.setContextFlags(jq_RegisterState.CONTEXT_CONTROL);
                    b = other_nt.setContext(regs);
                    if (!b) {
                        if (TRACE) SystemInterface.debugwriteln("Failed to set thread context for " + other_nt);
                    } else {
                        if (TRACE) SystemInterface.debugwriteln(other_nt + " : simulating a call to threadSwitch");
                    }
                }
            } else {
                if (STATISTICS) ++disabledCount;
            }
            other_nt.resume();
        }
    }
