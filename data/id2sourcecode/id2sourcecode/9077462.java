    public void itemStateChanged() {
        ErrorMsg.reportStatus("GP16JointPolice: Got event, checking says " + sendOk());
        if (sendOk()) ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
    }
