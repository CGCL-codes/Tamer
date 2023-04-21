            public void actionPerformed(ActionEvent e) {
                SCLCmTune scltun = new SCLCmTune(myDoc);
                if (myDoc.numberOfCav > 0) {
                    String[] phs = new String[myDoc.numberOfCav];
                    String bmgate = "ICS_Tim:Util:event46Count";
                    for (int i = 0; i < myDoc.numberOfCav; i++) {
                        phs[i] = myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfP";
                    }
                    if (scltun.setPV1(phs) && scltun.setPV3(bmgate)) {
                        Thread t = new Thread(scltun);
                        t.start();
                    }
                }
            }
