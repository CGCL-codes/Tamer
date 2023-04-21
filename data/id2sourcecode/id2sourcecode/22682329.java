    public boolean handleWrite() {
        if (currentState != null) {
            if (currentState.isReading()) {
                LOG.warn("Got a write notification while reading");
                writeSink.interestWrite(this, false);
                return false;
            } else {
                return processCurrentState(currentState, false);
            }
        } else {
            LOG.warn("Got a write notification with no current state");
            writeSink.interestWrite(this, false);
            return false;
        }
    }
