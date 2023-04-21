    private void getallparameters() {
        Channel ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_K");
        try {
            affk = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Kp");
        try {
            affkp = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Ki");
        try {
            affki = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Shift");
        try {
            afftimeshift = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Start");
        try {
            affstart = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Duration");
        try {
            affduration = ca.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFFVetoMax");
        try {
            affmax = ca.getValInt();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
        ca = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFFAvgN");
        try {
            affavg = ca.getValInt();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error " + ce);
        } catch (GetException ge) {
            myDoc.errormsg("Error " + ge);
        }
    }
