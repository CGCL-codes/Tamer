    protected final void release() {
        if (Options.verbose >= 4) Log.writeln("  Preparing all collector threads for termination");
        int order = VM_Interface.rendezvous(4270);
        baseThreadLocalRelease(order);
        if (order == 1) {
            int count = 0;
            for (int i = 0; i < planCount; i++) {
                Plan p = plans[i];
                if (VM_Interface.isNonParticipating(p)) {
                    count++;
                    ((StopTheWorldGC) p).baseThreadLocalRelease(NON_PARTICIPANT);
                }
            }
            if (Options.verbose >= 4) {
                Log.write("  There were ");
                Log.write(count);
                Log.writeln(" non-participating GC threads");
            }
        }
        order = VM_Interface.rendezvous(4280);
        if (order == 1) {
            baseGlobalRelease();
            setGcStatus(NOT_IN_GC);
        }
        VM_Interface.rendezvous(4290);
    }
