    protected void initCavity() {
        if (myDoc.selected != null) numberOfCav = myDoc.selected.size();
        myDoc.numberOfCav = numberOfCav;
        if (numberOfCav == 0) {
            myDoc.errormsg("Error, 0 cavity been selected!");
            return;
        }
        myDoc.selectedCav = new String[numberOfCav];
        myDoc.cav = new SCLCavity[numberOfCav];
        if (numberOfCav > 1) {
            String tmp;
            for (int i = 0; i < numberOfCav; i++) myDoc.selectedCav[i] = (String) myDoc.selected.get(i);
            for (int i = 0; i < numberOfCav; i++) {
                for (int j = i + 1; j < numberOfCav; j++) {
                    if (myDoc.selectedCav[i].compareTo(myDoc.selectedCav[j]) > 0) {
                        tmp = myDoc.selectedCav[i];
                        myDoc.selectedCav[i] = myDoc.selectedCav[j];
                        myDoc.selectedCav[j] = tmp;
                    }
                }
            }
            for (int i = 0; i < numberOfCav; i++) {
                myDoc.cav[i] = (SCLCavity) (mySeq.getNodeWithId(myDoc.selectedCav[i]));
            }
        } else {
            myDoc.selectedCav[0] = (String) myDoc.selected.get(0);
            myDoc.cav[0] = (SCLCavity) (mySeq.getNodeWithId(myDoc.selectedCav[0]));
        }
        resetcav();
        myDoc.getMonitor().reset();
        ChannelFactory caF = ChannelFactory.defaultFactory();
        try {
            Channel ca = caF.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
            myDoc.llrfDt = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error, cannot connect to Wf_Dt PV!");
        } catch (GetException ge) {
            myDoc.errormsg("Error, cannot get Dt value!");
        } finally {
        }
        if (myDoc.getMonitor().monitorPanel == null) {
            myDoc.getMonitor().monitorPanel = myDoc.getMonitor().makeMonitorPanel();
            myDoc.myWindow().getMainPanel().add("Monitor", myDoc.getMonitor().monitorPanel);
        } else {
            if (boxPanel != null) myDoc.getMonitor().monitorPanel.remove(boxPanel); else myDoc.getMonitor().monitorPanel.remove(myDoc.getMonitor().boxPanel);
            boxPanel = myDoc.getMonitor().makeboxPanel();
            myDoc.getMonitor().monitorPanel.add(boxPanel, BorderLayout.WEST);
        }
        if (controlPanel == null) {
            controlPanel = myDoc.getController().makeControlPanel();
            selectPanel.add(controlPanel);
        } else {
            selectPanel.setVisible(false);
            selectPanel.remove(controlPanel);
            controlPanel = myDoc.getController().makeControlPanel();
            selectPanel.add(controlPanel);
            selectPanel.setVisible(true);
        }
        myDoc.myWindow().repaint();
        return;
    }
