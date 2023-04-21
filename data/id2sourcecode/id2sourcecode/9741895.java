            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextRB.setText(null);
                if (scanVariable != null && scanOn == false) {
                    scanValueRB = scanVariable.getValueRB();
                    if (scanVariable.getChannelRB() != null) {
                        scanValueRB = scanVariable.getValueRB();
                        valueTextRB.setText(valueFormat.format(scanValueRB));
                    }
                }
            }
