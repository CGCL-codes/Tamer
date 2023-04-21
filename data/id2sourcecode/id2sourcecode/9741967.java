            public void run() {
                synchronized (lockObj) {
                    scanOn = true;
                    measurementThread = Thread.currentThread();
                    scanValueMem = scanValue;
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        scanVariable.memorizeValue();
                    }
                    trueMeasure(val);
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        if (restoreValueAfterScanButton.isSelected() == true) {
                            scanVariable.restoreFromMemory();
                        }
                    }
                    if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                        try {
                            lockObj.wait((long) (1000.0 * sleepTime));
                        } catch (InterruptedException e) {
                        }
                        if (scanVariable.getChannel() != null) {
                            setCurrentValue(scanVariable.getValue());
                            if (scanVariable.getChannelRB() != null) {
                                setCurrentValueRB(scanVariable.getValueRB());
                            } else {
                                valueTextRB.setText(null);
                                scanValueRB = scanValue;
                            }
                        } else {
                            valueTextRB.setText(null);
                            setCurrentValue(scanValueMem);
                            scanValueRB = scanValue;
                        }
                    } else {
                        valueTextRB.setText(null);
                        setCurrentValue(scanValueMem);
                        scanValueRB = scanValue;
                    }
                    scanOn = false;
                }
            }
