    public static void scanQueuedThreads(jq_NativeThread nt) {
        for (int i = 0; i < jq_NativeThread.NUM_OF_QUEUES; ++i) {
            if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning thread queue ", i);
            scanThreadQueue(nt.getReadyQueue(i));
        }
        if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning idle queue");
        scanThreadQueue(nt.getIdleQueue());
        if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning transfer queue");
        scanThreadQueue(nt.getTransferQueue());
    }
