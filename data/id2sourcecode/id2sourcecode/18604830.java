    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Play")) {
            startmonitor();
            update.setText("Stop");
        } else if (ae.getActionCommand().equals("Stop")) {
            stopmonitor();
            update.setText("Play");
        } else if (ae.getActionCommand().equals("K,Kp,Ki")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_K");
            try {
                ca.putVal(tfk.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg(myDoc.selectedCav[0] + " K " + ce);
            } catch (PutException ge) {
                myDoc.errormsg(myDoc.selectedCav[0] + " K " + ge);
            }
            ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Kp");
            try {
                ca.putVal(tfkp.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Kp " + ce);
            } catch (PutException ge) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Kp " + ge);
            }
            ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Ki");
            try {
                ca.putVal(tfki.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Ki " + ce);
            } catch (PutException ge) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Ki " + ge);
            }
        } else if (ae.getActionCommand().equals("Start")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Start");
            try {
                ca.putVal(tfstart.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Start " + ce);
            } catch (PutException ge) {
                myDoc.errormsg(myDoc.selectedCav[0] + " Start " + ge);
            }
        } else if (ae.getActionCommand().equals("Duration")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Offset");
            try {
                ca.putVal(tfduration.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
            ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "CtlRFPW.PROC");
            try {
                ca.putVal(1);
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
        } else if (ae.getActionCommand().equals("TShift")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Shift");
            try {
                ca.putVal(tftimeshift.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
        } else if (ae.getActionCommand().equals("Max.")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFFVetoMax");
            try {
                ca.putVal(tfmax.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
        } else if (ae.getActionCommand().equals("Wf. Avg")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFFAvgN");
            try {
                ca.putVal(tfavg.getValue());
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
        } else if (ae.getActionCommand().equals("Reset")) {
            Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Reset.PROC");
            try {
                ca.putVal(1);
            } catch (ConnectionException ce) {
                myDoc.errormsg("Error " + ce);
            } catch (PutException ge) {
                myDoc.errormsg("Error " + ge);
            }
        }
    }
