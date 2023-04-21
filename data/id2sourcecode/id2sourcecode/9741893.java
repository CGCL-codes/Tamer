            public void actionPerformed(ActionEvent e) {
                if (scanOn == false) {
                    try {
                        scanValue = Double.parseDouble(valueText.getText());
                    } catch (NumberFormatException exc) {
                    }
                    setCurrentValue(scanValue);
                    boolean containersCreated = false;
                    for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                        MeasuredValue mv_tmp = measuredValuesV.get(i);
                        if (mv_tmp != null && mv_tmp.getNumberOfDataContainers() == 0) {
                            mv_tmp.createNewDataContainer();
                            containersCreated = true;
                            if (scanVariable.getChannelRB() != null && mv_tmp.getNumberOfDataContainersRB() == 0) {
                                mv_tmp.createNewDataContainerRB();
                            }
                        }
                    }
                    if (containersCreated) {
                        for (int i = 0, n = newSetOfDataListenersV.size(); i < n; i++) {
                            newSetOfDataListenersV.get(i).actionPerformed(newSetOfDataAction);
                        }
                    }
                    measure(scanValue);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    setCurrentValue(scanValue);
                }
            }
