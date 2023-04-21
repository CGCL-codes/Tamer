    private void startNewSetOfData() {
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            ((MeasuredValue) measuredValuesV.get(i)).createNewDataContainer();
        }
        if (scanVariable != null && scanVariable.getChannelRB() != null) {
            for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                ((MeasuredValue) measuredValuesV.get(i)).createNewDataContainerRB();
            }
        }
        for (int i = 0, n = newSetOfDataListenersV.size(); i < n; i++) {
            ((ActionListener) newSetOfDataListenersV.get(i)).actionPerformed(newSetOfDataAction);
        }
    }
