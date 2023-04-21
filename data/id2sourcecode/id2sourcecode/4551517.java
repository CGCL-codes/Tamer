    private void setParamPV(double paramVal) {
        setParamCurrentValue(paramVal);
        paramValueRB = paramValue;
        if (paramVariable != null && paramVariable.getChannel() != null) {
            paramVariable.setValue(paramVal);
        }
        if (scanOn != false && sleepTime > 0.) {
            try {
                lockObj.wait((long) (1000.0 * sleepTime));
            } catch (InterruptedException e) {
            }
        }
        if (paramVariable != null && paramVariable.getChannelRB() != null && paramVariable.getMonitoredPV_RB().isGood()) {
            setParamCurrentValueRB(paramVariable.getValueRB());
        } else {
            paramTextRB.setText(null);
        }
    }
