    public CAMonitor(PVPanel pvPanel) {
        myPVPanel = pvPanel;
        pvName = pvPanel.getPVName();
        ca = caF.getChannel(pvName);
        ca.connectAndWait();
        connected = ca.isConnected();
        ca.addConnectionListener(new ConnectionListener() {

            public void connectionMade(Channel aChannel) {
                connected = true;
            }

            public void connectionDropped(Channel aChannel) {
                connected = false;
            }
        });
        nf.setMaximumFractionDigits(5);
        plotData.setImmediateContainerUpdate(false);
    }
