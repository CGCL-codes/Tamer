    protected void initCavity(ActionEvent ae) {
        selectedCav = cavTableModel.getRowName(cavTable.getSelectedRow());
        cavSelector.setVisible(false);
        rfCav = (SCLCavity) (mySeq.getNodeWithId(selectedCav));
        myDoc.getSCLPhase().setCavity(rfCav);
        myDoc.getSCLPhase().cavName.setForeground(Color.blue);
        myDoc.getSCLPhase().cavName.setText("Cavity: " + selectedCav);
        int cavInd = Integer.parseInt(rfCav.getId().substring(10, 12));
        if (cavInd < 12) {
            myDoc.getSCLPhase().cavType.setSelectedIndex(1);
        } else {
            myDoc.getSCLPhase().cavType.setSelectedIndex(0);
        }
        myDoc.getSCLPhase().tfAccFld.setValue(rfCav.getDfltCavAmp() * rfCav.getStructureTTF());
        nf.setMaximumFractionDigits(1);
        myDoc.getSCLPhase().tfQuality.setText(nf.format(rfCav.getQLoaded()));
        myDoc.getSCLPhase().tfPhaseSt.setValue(rfCav.getDfltAvgCavPhase());
        myDoc.getSCLPhase().setFrequency(rfCav.getCavFreq());
        double eng = ((EnvelopeTrajectory) myDoc.getScenario().getTrajectory()).stateForElement(selectedCav).getKineticEnergy() / 1.e6;
        myDoc.getSCLPhase().tfEnergy.setValue(eng);
        ChannelFactory caF = ChannelFactory.defaultFactory();
        if (myDoc.isOnline) {
            try {
                Channel ca = caF.getChannel("ICS_Tim:Gate_BeamRef:GateWidth");
                myDoc.getSCLPhase().tfPulse.setValue(ca.getValDbl());
                Channel ca1 = caF.getChannel(rfCav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 9) + "ResCtrl" + rfCav.channelSuite().getChannel("cavAmpSet").getId().substring(12, 15) + ":ResErr_Avg");
                myDoc.getSCLPhase().tfDetuning.setValue(ca1.getValDbl());
                Channel ca2 = caF.getChannel(rfCav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
                pulseWidthdt = ca2.getValDbl();
                myDoc.llrfx = new double[512];
                for (int i = 0; i < 512; i++) myDoc.llrfx[i] = pulseWidthdt * i;
                Channel ca3 = caF.getChannel(rfCav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfP");
                Channel ca4 = caF.getChannel(rfCav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfA");
                myDoc.setpv(ca3, ca4);
                Channel cavFieldSetC = null;
                cavFieldSetC = rfCav.lazilyGetAndConnect(RfCavity.CAV_AMP_SET_HANDLE, cavFieldSetC);
                try {
                    Monitor cavFieldMonitor = cavFieldSetC.addMonitorValTime(new IEventSinkValTime() {

                        public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                            double cavField = newRecord.doubleValue();
                            myDoc.getSCLPhase().cavFieldSetPt(cavField);
                            if (cavField > 0.5) myDoc.getSCLPhase().cavFieldSP.setForeground(Color.RED);
                            myDoc.getSCLPhase().cavFieldSP.setText("Field Set Pt.: " + nf.format(cavField));
                        }
                    }, Monitor.VALUE);
                } catch (ConnectionException e) {
                    System.out.println("Cannot connect to " + cavFieldSetC.getId());
                } catch (MonitorException e) {
                }
                Channel cavPhaseSetC = null;
                cavPhaseSetC = rfCav.lazilyGetAndConnect(RfCavity.CAV_PHASE_SET_HANDLE, cavPhaseSetC);
                try {
                    Monitor cavPhaseMonitor = cavPhaseSetC.addMonitorValTime(new IEventSinkValTime() {

                        public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                            double cavPhase = newRecord.doubleValue();
                            myDoc.getSCLPhase().cavPhaseSP.setText("Phase Set Pt.: " + nf.format(cavPhase));
                        }
                    }, Monitor.VALUE);
                } catch (ConnectionException e) {
                    System.out.println("Cannot connect to " + cavPhaseSetC.getId());
                } catch (MonitorException e) {
                }
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to PV(s)!");
            } catch (GetException ge) {
                System.out.println("Cannot get PV value(s)!");
            }
            myDoc.getSCLPhase().lbPhaseBm.setEnabled(true);
            myDoc.getSCLPhase().btPulse.setEnabled(true);
            myDoc.getSCLPhase().cavOff.setEnabled(true);
            if (myDoc.fullRecord) myDoc.getSCLPhase().phaseAvgBtn.setEnabled(true); else myDoc.getSCLPhase().phaseAvgBtn.setEnabled(false);
            myDoc.getSCLPhase().phaseAvgBtn1.setEnabled(true);
        }
        try {
            CurrentMonitor bcm = (CurrentMonitor) (mySeq.getAllNodesOfType("BCM").get(0));
            Channel beamIAvgC = null;
            beamIAvgC = bcm.lazilyGetAndConnect(CurrentMonitor.I_AVG_HANDLE, beamIAvgC);
            try {
                Monitor beamIAvgMonitor = beamIAvgC.addMonitorValTime(new IEventSinkValTime() {

                    public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                        double beamIAvg = newRecord.doubleValue();
                        if (myDoc.isOnline) {
                            myDoc.getSCLPhase().tfCurrent.setText(nf.format(beamIAvg));
                        } else {
                            myDoc.getSCLPhase().lbPhaseBm.setEnabled(false);
                            myDoc.getSCLPhase().btPulse.setEnabled(false);
                            myDoc.getSCLPhase().cavOff.setEnabled(false);
                            myDoc.getSCLPhase().phaseAvgBtn.setEnabled(false);
                            myDoc.getSCLPhase().phaseAvgBtn1.setEnabled(false);
                        }
                    }
                }, Monitor.VALUE);
            } catch (ConnectionException e) {
                System.out.println("Cannot connect to " + beamIAvgC.getId());
            } catch (MonitorException e) {
                System.out.println("CurrentMonitor error " + beamIAvgC.getId());
            }
        } catch (NullPointerException ne) {
            System.out.println("No BCM available in the selected sequence");
        } catch (ConnectionException ce) {
            System.out.println("Cannot connect to PV(s)!");
        } catch (NoSuchChannelException nse) {
            System.out.println("Cannot connect to BCM!");
            myDoc.getSCLPhase().tfCurrent.setValue(20.);
        }
    }
