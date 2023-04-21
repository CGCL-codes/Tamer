    static final void moveToFinalizable() throws VM_PragmaUninterruptible {
        if (TRACE) VM_Scheduler.trace(" VM_Finalizer: ", " move to finalizable ");
        boolean added = false;
        boolean is_live = false;
        foundFinalizableObject = false;
        foundFinalizableCount = 0;
        int initial_finalize_count = finalize_count;
        VM_FinalizerListElement le = live_head;
        VM_FinalizerListElement from = live_head;
        while (le != null) {
            is_live = VM_Allocator.processFinalizerListElement(le);
            if (is_live) {
                from = le;
                le = le.next;
                continue;
            } else {
                added = true;
                live_count--;
                finalize_count++;
                if (TRACE) VM_Scheduler.trace("\n moving to finalizer:", "le.value =", VM_Magic.objectAsAddress(le.pointer));
                VM_FinalizerListElement current = le.next;
                if (le == live_head) live_head = current; else from.next = current;
                VM_FinalizerListElement temp = finalize_head;
                finalize_head = le;
                le.next = temp;
                le = current;
            }
        }
        if (added) {
            if (TRACE) VM_Scheduler.trace(" VM_Finalizer: ", " added was true");
            foundFinalizableObject = true;
            foundFinalizableCount = finalize_count - initial_finalize_count;
            if (!VM_Scheduler.finalizerQueue.isEmpty()) {
                VM_Thread tt = VM_Scheduler.finalizerQueue.dequeue();
                VM_Processor.getCurrentProcessor().scheduleThread(tt);
            }
        }
        if (PRINT_FINALIZABLE_COUNT && VM_Allocator.verbose >= 1) {
            VM.sysWrite("<GC ");
            VM.sysWrite(VM_Collector.collectionCount(), false);
            VM.sysWrite(" moveToFinalizable: finalize_count: before = ");
            VM.sysWrite(initial_finalize_count, false);
            VM.sysWrite(" after = ");
            VM.sysWrite(finalize_count, false);
            VM.sysWrite(">\n");
        }
    }
