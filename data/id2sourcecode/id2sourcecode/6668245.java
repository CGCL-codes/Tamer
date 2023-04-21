    protected void notifyEEGReadListeners() {
        if (readListeners.isEmpty()) {
            return;
        }
        EEGReadEvent e = new EEGReadEvent(this, getCurrentChannelValues());
        if (log.isDebugEnabled()) {
            for (EEGChannelValue value : e.getChannels()) {
                log.debug("Setting current calibrated value " + value.getChannelStrengthWithCalibration() + " for channel " + value.getChannelState().getFrequencyType().name());
            }
        }
        for (EEGReadListener readListener : readListeners) {
            readListener.readEventPerformed(e);
        }
    }
