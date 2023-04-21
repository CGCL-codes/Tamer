    public static void dumpVirtualMachine() throws VM_PragmaInterruptible {
        VM_Processor processor;
        writeString("\n-- Processors --\n");
        for (int i = 1; i <= numProcessors; ++i) {
            processor = processors[i];
            processor.dumpProcessorState();
        }
        if (VM.BuildWithNativeDaemonProcessor) {
            writeString("\n-- NativeDaemonProcessor --\n");
            processors[nativeDPndx].dumpProcessorState();
        }
        writeString("\n-- Native Processors --\n");
        for (int i = 1; i <= VM_Processor.numberNativeProcessors; i++) {
            processor = VM_Processor.nativeProcessors[i];
            if (processor == null) {
                writeString(" NULL processor for nativeProcessors entry = ");
                writeDecimal(i);
                continue;
            }
            processor.dumpProcessorState();
        }
        writeString("\n-- System Queues -- \n");
        wakeupQueue.dump();
        writeString(" wakeupQueue:");
        wakeupQueue.dump();
        writeString(" debuggerQueue:");
        debuggerQueue.dump();
        writeString(" deadVPQueue:");
        deadVPQueue.dump();
        writeString(" collectorQueue:");
        collectorQueue.dump();
        writeString(" finalizerQueue:");
        finalizerQueue.dump();
        writeString(" nativeProcessorQueue:");
        nativeProcessorQueue.dump();
        writeString("\n-- Threads --\n");
        for (int i = 1; i < threads.length; ++i) if (threads[i] != null) threads[i].dump();
        writeString("\n");
        writeString("\n-- Locks available --\n");
        for (int i = PRIMORDIAL_PROCESSOR_ID; i <= numProcessors; ++i) {
            processor = processors[i];
            int unallocated = processor.lastLockIndex - processor.nextLockIndex + 1;
            writeString(" processor ");
            writeDecimal(i);
            writeString(": ");
            writeDecimal(processor.locksAllocated);
            writeString(" locks allocated, ");
            writeDecimal(processor.locksFreed);
            writeString(" locks freed, ");
            writeDecimal(processor.freeLocks);
            writeString(" free looks, ");
            writeDecimal(unallocated);
            writeString(" unallocated slots\n");
        }
        writeString("\n");
        writeString("\n-- Locks in use --\n");
        for (int i = 0; i < locks.length; ++i) if (locks[i] != null) locks[i].dump();
        writeString("\n");
    }
