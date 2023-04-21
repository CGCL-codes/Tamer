    public void PVSelected(java.awt.event.ActionEvent evt) {
        gov.sns.tools.apputils.PVSelection.PVSelector pvS = (gov.sns.tools.apputils.PVSelection.PVSelector) evt.getSource();
        Channel thePV = pvS.getSelectedChannel();
        if (thePV == null) {
            thePV = ChannelFactory.defaultFactory().getChannel(pvS.getPVText());
        }
        if (pvS.getLabel().equals("X PV")) {
            setXPV(thePV);
        } else if (pvS.getLabel().equals("Y PV")) {
            setYPV(thePV);
        } else if (pvS.getLabel().equals("Z PV")) {
            setZPV(thePV);
        }
        setHasChanges(true);
    }
