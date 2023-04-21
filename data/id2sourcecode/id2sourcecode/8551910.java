            public void actionPerformed(ActionEvent e) {
                double ampVal = guessAmpP0_Text.getValue();
                double phaseVal = guessPhaseP0_Text.getValue();
                if (scanVariableParameter.getChannel() != null && scanVariable.getChannel() != null) {
                    scanVariableParameter.setValue(ampVal);
                    scanVariable.setValue(phaseVal);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("The parameter or Scan Variable PV channel does not exist.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
