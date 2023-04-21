    public void measure() {
        Runnable runMeasure = new Runnable() {

            public void run() {
                synchronized (lockObj) {
                    scanOn = true;
                    measurementThread = Thread.currentThread();
                    if (scanVarShouldBeMemorized == true) {
                        if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                            scanVariable.memorizeValue();
                        }
                        scanValueMem = scanValue;
                    }
                    trueMeasure();
                    if (scanVarShouldBeRestored == true && scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        if (restoreValueAfterScanButton.isSelected() == true) {
                            scanVariable.restoreFromMemory();
                        }
                    }
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        if (scanOn != false && sleepTime > 0.) {
                            try {
                                lockObj.wait((long) (1000.0 * sleepTime));
                            } catch (InterruptedException e) {
                            }
                        }
                        if (scanVariable.getChannel() != null) {
                            Thread localUpDateThread = new Thread(new Runnable() {

                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                    setCurrentValue(scanVariable.getValue());
                                    if (scanVariable.getChannelRB() != null) {
                                        setCurrentValueRB(scanVariable.getValueRB());
                                    } else {
                                        valueTextRB.setText(null);
                                    }
                                }
                            });
                            localUpDateThread.start();
                        } else {
                            valueTextRB.setText(null);
                            if (scanVarShouldBeRestored == true) {
                                setCurrentValue(scanValueMem);
                            } else {
                                setCurrentValue(scanValue);
                            }
                        }
                    } else {
                        valueTextRB.setText(null);
                        if (scanVarShouldBeRestored == true) {
                            setCurrentValue(scanValueMem);
                        } else {
                            setCurrentValue(scanValue);
                        }
                    }
                    if (continueMode) {
                        setButtonsState(RESUME_BUTTONS_STATE);
                    } else {
                        setButtonsState(START_BUTTONS_STATE);
                    }
                    scanOn = false;
                    for (int i = 0, n = stopListenersV.size(); i < n; i++) {
                        stopListenersV.get(i).actionPerformed(stopButtonAction);
                    }
                }
            }
        };
        Thread mThread = new Thread(runMeasure);
        mThread.start();
    }
