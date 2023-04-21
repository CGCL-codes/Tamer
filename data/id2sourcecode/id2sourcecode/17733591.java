    @Override
    public synchronized void end() {
        checkStateForRead("end");
        switch(mode) {
            case readonly:
                reactiveBigraph.handleEnd(this, currentValue, false);
                break;
            case readwrite:
                reactiveBigraph.handleEnd(this, currentValue, state == State.changed);
                break;
        }
        state = State.idle;
        currentValue = null;
    }
