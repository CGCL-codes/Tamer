    private void accountNewDataPoint() {
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            ((MeasuredValue) measuredValuesV.get(i)).consumeData(scanValue);
        }
        if (scanVariable != null && scanVariable.getChannelRB() != null) {
            for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                ((MeasuredValue) measuredValuesV.get(i)).consumeDataRB(scanValueRB);
            }
        }
        for (int i = 0, n = newPointOfDataListenersV.size(); i < n; i++) {
            ((ActionListener) newPointOfDataListenersV.get(i)).actionPerformed(newPointOfDataAction);
        }
    }
