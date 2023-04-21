            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    ch = cf.getChannel(((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                    try {
                        ch.putVal(0);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connection " + ch.getId());
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write to PV " + ch.getId());
                    }
                }
            }
