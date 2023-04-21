    public static void scanAllThreads() {
        if (jq_NativeThread.allNativeThreadsInitialized()) {
            for (int i = 0; i < jq_NativeThread.native_threads.length; ++i) {
                jq_NativeThread nt = jq_NativeThread.native_threads[i];
                if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning native thread ", i);
                scanQueuedThreads(nt);
            }
        } else {
            jq_NativeThread nt = Unsafe.getThreadBlock().getNativeThread();
            if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning initial native thread");
            scanQueuedThreads(nt);
        }
        scanCurrentThreadStack(3);
    }
