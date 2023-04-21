    public void startmonitor() {
        double dx = bcmDt * 1.E6;
        cavx = new double[512];
        bcmx = new double[1120];
        for (int i = 0; i < bcmx.length; i++) bcmx[i] = dx * i;
        for (int i = 0; i < cavx.length; i++) cavx[i] = llrfDt * i;
        bcmC = cf.getChannel(bcmWavePv);
        cavF = cf.getChannel(myDoc.cav[currentCAV].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfA");
        cavP = cf.getChannel(myDoc.cav[currentCAV].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfP");
        try {
            cavFieldMonitor = cavF.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    cavField = newRecord.doubleArray();
                }
            }, Monitor.VALUE);
            cavPhaseMonitor = cavP.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    cavPhase = newRecord.doubleArray();
                    if (trans) convert();
                    amplitude.setPoints(cavx, cavField);
                    phase.setPoints(cavx, cavPhase);
                    plotSignal.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error, in connection " + cavF.getId() + " or " + cavP.getId());
        } catch (MonitorException me) {
            myDoc.errormsg("Error, in LLRF waveform monitor " + me);
        }
        try {
            bcmMonitor = bcmC.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    bcmWave = newRecord.doubleArray();
                    pulse.setPoints(bcmx, bcmWave);
                    plotBeam.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error, in connection " + bcmC.getId());
        } catch (MonitorException me) {
            myDoc.errormsg("Error, in beam current monitor " + me);
        }
    }
