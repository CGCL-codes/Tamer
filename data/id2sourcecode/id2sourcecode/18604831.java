    public void startmonitor() {
        double dx = 0.;
        Channel dt = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
        try {
            dx = dt.getValDbl();
        } catch (ConnectionException ce) {
            myDoc.errormsg(ce + " Wf_Dt\n");
        } catch (GetException ge) {
            myDoc.errormsg(ge + " Wf_Dt\n");
        }
        cavx = new double[512];
        for (int i = 0; i < cavx.length; i++) {
            cavx[i] = dx * i;
        }
        Channel cavA = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfA");
        Channel cavP = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfP");
        Channel fwdA = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Fwd_WfA");
        Channel fwdP = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Fwd_WfP");
        Channel errI = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Err_I");
        Channel errQ = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Err_Q");
        try {
            campMonitor = cavA.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    cva = newRecord.doubleArray();
                    cava.setPoints(cavx, cva);
                    plotamp.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
            cphsMonitor = cavP.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    cvp = newRecord.doubleArray();
                    cavp.setPoints(cavx, cvp);
                    plotphs.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
            fampMonitor = fwdA.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    fpa = newRecord.doubleArray();
                    fwda.setPoints(cavx, fpa);
                    plotamp.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
            fphsMonitor = fwdP.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    fpp = newRecord.doubleArray();
                    fwdp.setPoints(cavx, fpp);
                    plotphs.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
            ierrMonitor = errI.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    eri = newRecord.doubleArray();
                    erri.setPoints(cavx, eri);
                    ploterr.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
            qerrMonitor = errQ.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    erq = newRecord.doubleArray();
                    errq.setPoints(cavx, erq);
                    ploterr.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error, in connection Amp or Err");
        } catch (MonitorException me) {
            myDoc.errormsg("Error, in LLRF waveform monitor " + me);
        }
    }
