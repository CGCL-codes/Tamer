    public final void baseThreadLocalPrepare(int order) {
        if (order == NON_PARTICIPANT) {
            VM_Interface.prepareNonParticipating((Plan) this);
        } else {
            VM_Interface.prepareParticipating((Plan) this);
            VM_Interface.rendezvous(4260);
        }
        if (Options.verbose >= 4) Log.writeln("  Preparing all collector threads for start");
        threadLocalPrepare(order);
    }
