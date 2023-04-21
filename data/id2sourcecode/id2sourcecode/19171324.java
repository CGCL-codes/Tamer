    jq_InterrupterThread(jq_NativeThread other_nt) {
        this.other_nt = other_nt;
        if (TRACE) SystemInterface.debugwriteln("Initialized timer interrupt for native thread " + other_nt);
        myself = ThreadUtils.getJQThread(this);
        myself.disableThreadSwitch();
        this.tid = SystemInterface.create_thread(_run.getDefaultCompiledVersion().getEntrypoint(), HeapAddress.addressOf(this));
        jq_NativeThread my_nt = new jq_NativeThread(myself);
        my_nt.getCodeAllocator().init();
        my_nt.getHeapAllocator().init();
        SystemInterface.resume_thread(this.tid);
    }
