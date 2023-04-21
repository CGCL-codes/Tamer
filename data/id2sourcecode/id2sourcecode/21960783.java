            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.numberOfCav; i++) {
                    try {
                        Channel ca2 = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RunState");
                        ca2.putVal("Ramp");
                        Channel ca3 = cf.getChannel("SCL_HPRF:Tun" + myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Tun_Ctl");
                        ca3.putVal("Auto-Tune");
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error, cannot connect to PV!");
                    } catch (PutException pe) {
                        myDoc.errormsg("Error, cannot write to PV!");
                    }
                }
            }
