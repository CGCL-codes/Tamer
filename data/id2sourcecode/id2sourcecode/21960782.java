            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.numberOfCav; i++) {
                    try {
                        Channel LoopOff = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                        LoopOff.putVal("Close!");
                        Channel RFKill = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RFKill");
                        RFKill.putVal("Kill");
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error, cannot connect to PV!");
                    } catch (PutException pe) {
                        myDoc.errormsg("Error, cannot write to PV!");
                    }
                }
            }
