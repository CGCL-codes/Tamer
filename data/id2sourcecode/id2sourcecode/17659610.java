    private void startMonitors(String name) {
        ChannelFactory caF = ChannelFactory.defaultFactory();
        hom0StateCh = caF.getChannel(name + ":HBADC0_Ctl");
        hom1StateCh = caF.getChannel(name + ":HBADC1_Ctl");
        Channel hb0WF = caF.getChannel(name + ":HB0");
        Channel hb1WF = caF.getChannel(name + ":HB1");
        try {
            double[] tmpArry = hb0WF.getArrDbl();
            x1 = new double[tmpArry.length];
            hom0State = Integer.parseInt(hom0StateCh.getValueRecord().stringValue());
            hom1State = Integer.parseInt(hom1StateCh.getValueRecord().stringValue());
        } catch (GetException ge) {
        } catch (ConnectionException ce) {
        }
        for (int i = 0; i < x1.length; i++) {
            x1[i] = i;
        }
        try {
            hom0StateCh.putRawValCallback(4, new PutListener() {

                public void putCompleted(Channel ch) {
                }
            });
            hom1StateCh.putRawValCallback(5, new PutListener() {

                public void putCompleted(Channel ch) {
                }
            });
        } catch (ConnectionException ce) {
        } catch (PutException pe) {
        }
        try {
            hb0Monitor = hb0WF.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    hom0 = newRecord.doubleArray();
                    hom0PlotData.setPoints(x1, hom0);
                    hom0Plot.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException e) {
            System.out.println("Cannot connect to " + hb0WF.getId());
        } catch (MonitorException e) {
            System.out.println("Cannot monitor " + hb0WF.getId());
        }
        try {
            hb1Monitor = hb1WF.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    hom1 = newRecord.doubleArray();
                    hom1PlotData.setPoints(x1, hom1);
                    hom1Plot.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException e) {
            System.out.println("Cannot connect to " + hb1WF.getId());
        } catch (MonitorException e) {
            System.out.println("Cannot monitor " + hb1WF.getId());
        }
    }
