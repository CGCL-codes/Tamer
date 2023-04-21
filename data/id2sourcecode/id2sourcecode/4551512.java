            public void run() {
                synchronized (lockObj) {
                    scanOn = true;
                    measurementThread = Thread.currentThread();
                    if (scanVarShouldBeMemorized == true) {
                        if (paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                            paramVariable.memorizeValue();
                        }
                        paramValueMem = paramValue;
                    }
                    if (scanVarShouldBeMemorized == true) {
                        if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                            scanVariable.memorizeValue();
                        }
                        scanValueMem = scanValue;
                    }
                    if (continueMode == false) {
                        setVariableSet();
                        setParamVariableSet();
                        setParamPV(paramVariableSet[paramPositionInd]);
                        startNewSetOfData();
                    }
                    for (int pS = paramPositionInd; pS < paramNPoints; pS++) {
                        setParamPV(paramVariableSet[pS]);
                        if (positionInd == nPoints) {
                            positionInd = 0;
                            startNewSetOfData();
                        }
                        paramPositionInd = pS;
                        if (!trueMeasure()) {
                            continueMode = true;
                            break;
                        } else {
                            continueMode = false;
                            paramPositionInd++;
                        }
                    }
                    if (scanVarShouldBeRestored == true && paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                        paramVariable.restoreFromMemory();
                    }
                    if (scanVarShouldBeRestored == true && scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        scanVariable.restoreFromMemory();
                    }
                    if (scanOn != false && sleepTime > 0.) {
                        try {
                            lockObj.wait((long) (1000.0 * sleepTime));
                        } catch (InterruptedException e) {
                        }
                    }
                    if (scanVarShouldBeRestored == true) {
                        valueTextRB.setText(null);
                        paramTextRB.setText(null);
                    }
                    setCurrentValue(scanValueMem);
                    setParamCurrentValue(paramValueMem);
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
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
                                    }
                                }
                            });
                            localUpDateThread.start();
                        }
                    }
                    if (paramVariable != null && paramVariable.getMonitoredPV().isGood()) {
                        if (paramVariable.getChannel() != null) {
                            Thread localUpDateThread = new Thread(new Runnable() {

                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                    }
                                    setParamCurrentValue(paramVariable.getValue());
                                    if (paramVariable.getChannelRB() != null) {
                                        setParamCurrentValueRB(paramVariable.getValueRB());
                                    }
                                }
                            });
                            localUpDateThread.start();
                        }
                    }
                    if (continueMode) {
                        setButtonsState(RESUME_BUTTONS_STATE);
                    } else {
                        setButtonsState(START_BUTTONS_STATE);
                    }
                    scanOn = false;
                }
            }
