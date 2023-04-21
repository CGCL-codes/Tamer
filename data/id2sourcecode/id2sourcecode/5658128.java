    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("On")) {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                ch = cf.getChannel(((RfCavity) myDoc.cav[i]).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                try {
                    ch.putVal(2);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + ch.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + ch.getId());
                }
            }
        } else if (ae.getActionCommand().equals("Off")) {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                ch = cf.getChannel(((RfCavity) myDoc.cav[i]).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                try {
                    ch.putVal(0);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + ch.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + ch.getId());
                }
            }
        } else if (ae.getActionCommand().equals("Freeze")) {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                ch = cf.getChannel(((RfCavity) myDoc.cav[i]).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                try {
                    ch.putVal(1);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + ch.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + ch.getId());
                }
            }
        } else if (ae.getActionCommand().equals("Reset")) {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                ch = cf.getChannel(((RfCavity) myDoc.cav[i]).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Reset.PROC");
                try {
                    ch.putVal(1);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + ch.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + ch.getId());
                }
            }
        } else if (ae.getActionCommand().equals("Update")) {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                sclpv = ((RfCavity) myDoc.cav[i]).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16);
                update();
            }
        }
    }
