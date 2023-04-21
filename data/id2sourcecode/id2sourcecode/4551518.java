    private boolean trueMeasure(double val) {
        scanValue = val;
        if (scanVariable != null) {
            scanVariable.setValue(scanValue);
        }
        if (!scanOn) {
            return false;
        }
        if (sleepTime > 0.) {
            try {
                lockObj.wait((long) (1000.0 * sleepTime));
            } catch (InterruptedException e) {
            }
        }
        for (int k = 0, n = measuredValuesV.size(); k < n; k++) {
            if (!scanOn) {
                return false;
            }
            ((MeasuredValue) measuredValuesV.get(k)).restoreIniState();
        }
        if (!scanOn) {
            return false;
        }
        int j = 0;
        int badCount = 0;
        while (j < nAveraging) {
            badCount = 0;
            while (true) {
                if (validateMeasurements() && mpsStatusValidateMeasurements()) {
                    for (int k = 0, n = measuredValuesV.size(); k < n; k++) {
                        ((MeasuredValue) measuredValuesV.get(k)).measure();
                        if (!scanOn) {
                            return false;
                        }
                    }
                    j++;
                    break;
                } else if (mpsStatusValidateMeasurements()) {
                    if (!scanOn) {
                        Toolkit.getDefaultToolkit().beep();
                        return false;
                    }
                    scanOn = false;
                    scanVarShouldBeRestored = false;
                    if (measurementThread != null && measurementThread.isAlive()) {
                        measurementThread.interrupt();
                    }
                    JOptionPane.showMessageDialog(controllerPanel.getRootPane(), "The MPS has been masked.");
                }
                if (!scanOn) {
                    return false;
                }
                if (nAveraging > 1 && badCount > 0 && avrgTime > 0.) {
                    try {
                        lockObj.wait((long) (1000.0 * avrgTime));
                    } catch (InterruptedException e) {
                    }
                }
                if (nAveraging == 1 && badCount > 0) {
                    try {
                        lockObj.wait((long) (1000.0 * Math.max(sleepTime, avrgTime)));
                    } catch (InterruptedException e) {
                    }
                }
                badCount++;
                if (badCount > maxNumberBadMeasurements) {
                    scanOn = false;
                    messageText.setText("Cannot validate measurements.");
                }
                if (!scanOn) {
                    return false;
                }
            }
            if (!scanOn) {
                return false;
            }
            if (nAveraging > 1 && j != nAveraging && avrgTime > 0.) {
                try {
                    lockObj.wait((long) (1000.0 * avrgTime));
                } catch (InterruptedException e) {
                }
            }
            if (!scanOn) {
                return false;
            }
        }
        setCurrentValue(scanValue);
        if (scanVariable != null && scanVariable.getChannelRB() != null && scanVariable.getMonitoredPV_RB().isGood()) {
            setCurrentValueRB(scanVariable.getValueRB());
        } else {
            scanValueRB = scanValue;
            valueTextRB.setText(null);
        }
        accountNewDataPoint();
        return true;
    }
