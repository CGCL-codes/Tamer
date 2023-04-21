    public void measureAmp() {
        String amp;
        double[] am;
        for (int i = 0; i < myDoc.numberOfCav; i++) {
            amp = myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfA";
            myDoc.measuredAmp[i] = 0.0;
            try {
                Channel ca1 = cf.getChannel(amp);
                am = ca1.getArrDbl();
                for (int j = myDoc.startPt - 10; j < myDoc.startPt; j++) {
                    try {
                        myDoc.measuredAmp[i] += am[j] * 0.1;
                    } catch (NullPointerException ne) {
                        myDoc.errormsg("Error, out of beam pulse array index range!");
                    }
                }
                if (myDoc.measuredAmp[i] < 3000) {
                    myDoc.errormsg("Error, check " + amp + " setpoint!");
                }
            } catch (ConnectionException ce) {
                myDoc.errormsg(ce + " " + amp);
            } catch (GetException ge) {
                myDoc.errormsg(ge + " " + amp);
            } finally {
            }
            if (myDoc.beamAmp[i] < 100) {
                myDoc.errormsg("Error, check drifting beam signal!");
            }
            if (myDoc.beamAmp[i] != 0.) myDoc.cavAmpSt[i] = myDoc.beamLoad[i] * myDoc.measuredAmp[i] / myDoc.beamAmp[i];
        }
    }
