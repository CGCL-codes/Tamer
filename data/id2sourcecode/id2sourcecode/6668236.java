    public void calibrate() throws EEGException {
        setCalibrating(true);
        setStatusOfDevice("Device is calibrating");
        if (log.isDebugEnabled()) {
            log.debug("Calibrating " + getDeviceDescription());
        }
        final List<EEGChannelValue> values = new ArrayList<EEGChannelValue>();
        final EEGReadListener calibrationListener = new EEGReadListener() {

            public void readEventPerformed(EEGReadEvent e) {
                values.addAll(e.getChannels());
            }
        };
        addEEGReadListener(calibrationListener);
        Thread thread = new Thread("Calibration Thread") {

            public void run() {
                snooze(10000);
                removeEEGReadListener(calibrationListener);
                applyCalibration(values);
                setCalibrating(false);
                setStatusOfDevice("Device is calibrated");
                if (log.isDebugEnabled()) {
                    log.debug("Calibrated " + getDeviceDescription());
                }
            }
        };
        thread.start();
    }
