    protected void checkStateForUpdate(String operation) {
        if (state != State.active && state != State.changed) throw new BigraphRuntimeException("Cannot perform " + operation + " in state " + state);
        if (mode != Mode.readwrite) throw new BigraphRuntimeException("Cannot perform " + operation + " in " + mode + " mode");
    }
