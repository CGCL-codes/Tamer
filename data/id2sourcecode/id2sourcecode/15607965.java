    public void addTransitionOpns(Transition tran, Tape tape, HeadMove hm, Symbol read, Symbol write) throws IllegalParameterException, UnknownTapeException, UnknownTransitionException {
        if (tran == null || tape == null || hm == null) return;
        TransitionOpns to = new TransitionOpns(new HeadMove(hm.getMove()), read, write, tape);
        tran.addOpns(to);
        if (undoRedo != null && undoEnabled) undoRedo.performedOperation(this, new Operation(Operation.opType.TRANSITION_ADD_OPNS, new Object[] { to, tran }));
    }
