    public void clearCalibration() throws EEGException {
        synchronized (this) {
            List<EEGChannelState> states = getChannelStates();
            for (EEGChannelState state : states) {
                state.clearCalibration();
            }
        }
        setStatusOfDevice("Calibration cleared");
        if (log.isDebugEnabled()) {
            log.debug("Calibration cleared for " + getDeviceDescription());
        }
    }
