    private void setAmplitudeAndPhaseToEPICS() {
        if (analysisDone) {
            double ampVal = rfAmp_Text.getValue();
            double phaseVal = rfPhase_Text.getValue();
            if (scanVariableParameter.getChannel() != null && scanVariable.getChannel() != null) {
                scanVariableParameter.setValue(ampVal);
                scanVariable.setValue(phaseVal);
            } else {
                messageTextLocal.setText(null);
                messageTextLocal.setText("The parameter PV channel does not exist.");
                Toolkit.getDefaultToolkit().beep();
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
            messageTextLocal.setText(null);
            messageTextLocal.setText("Perform analysis first!");
            clearResultsOfAnalysis();
        }
    }
