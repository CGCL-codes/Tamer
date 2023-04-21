    public void itemStateChanged() {
        Logger.reportStatus("GP16JointPolice: Got event, checking says " + sendOk());
        if (sendOk()) {
            ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
        }
    }
