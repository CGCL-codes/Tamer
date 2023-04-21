    public void removeTransitionOpn(Transition tran, Tape t, HeadMove hm, Symbol read, Symbol write) throws UnknownTapeException, UnknownTransitionException, IllegalParameterException {
        if (!isTransition(tran)) throw new UnknownTransitionException(Turingmachine.class.getCanonicalName() + " / the passed transition is not part of this turingmachine / not removing the transitions opn");
        if (!isTape(t)) throw new UnknownTapeException(Turingmachine.class.getCanonicalName() + " / the passed tape is not part of this turingmachine / not removing the transitions opn");
        if (hm == null) throw new IllegalParameterException(Turingmachine.class.getCanonicalName() + " / the headmove has to be specified / not removing the transitions opn");
        if ((read == null || !isSymbol(read) && !read.isBlank() && !read.isEpsilon()) && (write == null || !isSymbol(write) && !write.isBlank() && !write.isEpsilon())) throw new IllegalParameterException(Turingmachine.class.getCanonicalName() + " / the passed symbol is not part of this turingmachine / not removing the transitions opn");
        TransitionOpns op = tran.removeOpn(t, hm, read, write);
        if (op != null) if (undoRedo != null && undoEnabled) undoRedo.performedOperation(this, new Operation(Operation.opType.TRANSITION_REMOVE_OPNS, new Object[] { op, tran }));
    }
