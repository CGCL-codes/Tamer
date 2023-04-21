    private void getDeltaT() {
        try {
            Channel ca1 = cf.getChannel(bcmDtPv);
            bcmDt = ca1.getValDbl() * 1.E-6;
            myDoc.bcmDt = bcmDt;
            Channel ca2 = cf.getChannel(myDoc.cav[currentCAV].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
            llrfDt = ca2.getValDbl();
            myDoc.llrfDt = llrfDt;
            myDoc.startPt = (int) Math.round(myDoc.endS / llrfDt);
            myDoc.pts = (int) Math.round((myDoc.endT - myDoc.endS) / llrfDt);
        } catch (ConnectionException ce) {
            myDoc.errormsg(ce + " Wf_Dt " + myDoc.cav[currentCAV]);
        } catch (GetException ge) {
            myDoc.errormsg(ge + " Wf_Dt " + myDoc.cav[currentCAV]);
        }
    }
