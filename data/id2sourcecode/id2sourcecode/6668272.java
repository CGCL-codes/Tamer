    public void setSignalProcessor(EEGSignalProcessor signalProcessor) {
        if (log.isDebugEnabled()) {
            log.debug("setSignalProcessor using " + signalProcessor.getDescription());
        }
        Class<? extends EEGSignalProcessor> existing = this.signalProcessor == null ? null : this.signalProcessor.getClass();
        Class<? extends EEGSignalProcessor> newSigProc = signalProcessor.getClass();
        if (existing != null && existing.equals(newSigProc)) {
            return;
        }
        for (EEGChannelState state : getChannelStates()) {
            state.setSignalProcessor(signalProcessor.getNewSignalProcessor());
        }
        this.signalProcessor = signalProcessor;
        setStatusOfDevice("Set signal processor to " + signalProcessor.getDescription());
    }
