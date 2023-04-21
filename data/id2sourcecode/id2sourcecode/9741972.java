    private void accountNewDataPoint() {
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            measuredValuesV.get(i).consumeData(phaseWrappingFunction(scanValue));
        }
        if (scanVariable != null && scanVariable.getChannelRB() != null) {
            for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                measuredValuesV.get(i).consumeDataRB(scanValueRB);
            }
        }
        for (int i = 0, n = newPointOfDataListenersV.size(); i < n; i++) {
            newPointOfDataListenersV.get(i).actionPerformed(newPointOfDataAction);
        }
    }
