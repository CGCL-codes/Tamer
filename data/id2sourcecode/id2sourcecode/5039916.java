    public static void collect() {
        if (true) Debug.writeln("Starting collection.");
        jq_Thread t = Unsafe.getThreadBlock();
        t.disableThreadSwitch();
        jq_NativeThread.suspendAllThreads();
        if (true) Debug.writeln("Threads suspended.");
        SimpleAllocator s = (SimpleAllocator) DefaultHeapAllocator.def();
        if (true) Debug.writeln("--> Marking roots.");
        scanRoots();
        if (true) Debug.writeln("--> Marking queue.");
        s.scanGCQueue();
        if (true) Debug.writeln("--> Sweeping.");
        s.sweep();
        if (true) Debug.writeln("Resuming threads.");
        jq_NativeThread.resumeAllThreads();
        t.enableThreadSwitch();
    }
