            public void run() {
                synchronized (lockObj) {
                    setButtonsState(SCAN_BUTTONS_STATE);
                    scanOn = true;
                    measurementThread = Thread.currentThread();
                    scanValueMem = scanValue;
                    paramValueMem = paramValue;
                    if (paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                        paramVariable.memorizeValue();
                    }
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        scanVariable.memorizeValue();
                    }
                    setVariableSet();
                    setParamPV(paramVal);
                    startNewSetOfData();
                    continueMode = false;
                    trueMeasure();
                    if (paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                        paramVariable.restoreFromMemory();
                    }
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        scanVariable.restoreFromMemory();
                    }
                    if (scanOn != false && sleepTime > 0.) {
                        try {
                            lockObj.wait((long) (1000.0 * sleepTime));
                        } catch (InterruptedException e) {
                        }
                    }
                    valueTextRB.setText(null);
                    paramTextRB.setText(null);
                    setCurrentValue(scanValueMem);
                    setParamCurrentValue(paramValueMem);
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        if (scanVariable.getChannel() != null) {
                            setCurrentValue(scanVariable.getValue());
                            if (scanVariable.getChannelRB() != null) {
                                setCurrentValueRB(scanVariable.getValueRB());
                            }
                        }
                    }
                    if (paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                        if (paramVariable.getChannel() != null) {
                            setParamCurrentValue(paramVariable.getValue());
                            if (paramVariable.getChannelRB() != null) {
                                setParamCurrentValueRB(paramVariable.getValueRB());
                            }
                        }
                    }
                    setButtonsState(START_BUTTONS_STATE);
                    scanOn = false;
                }
            }
