            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextRB.setText(null);
                if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                    if (scanVariable.getChannel() != null) {
                        setCurrentValue(scanVariable.getValue());
                    }
                    if (scanVariable.getChannelRB() != null) {
                        setCurrentValueRB(scanVariable.getValueRB());
                    }
                } else {
                    try {
                        scanValue = Double.parseDouble(valueText.getText());
                    } catch (NumberFormatException exc) {
                    }
                    ;
                    setCurrentValue(scanValue);
                }
            }
