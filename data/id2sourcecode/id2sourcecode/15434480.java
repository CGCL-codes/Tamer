    private void startMonitors(String name) {
        ChannelFactory caF = ChannelFactory.defaultFactory();
        if (typeInd == 0) {
            Channel bpmPhaseWF = caF.getChannel(name + ":beamPA");
            Channel bpmAmpWF = caF.getChannel(name + ":beamIA");
            try {
                double[] tmpArry = bpmPhaseWF.getArrDbl();
                x1 = new double[tmpArry.length];
            } catch (GetException ge) {
            } catch (ConnectionException ce) {
            }
            for (int i = 0; i < x1.length; i++) {
                x1[i] = i;
            }
            try {
                phaseArryMonitor = bpmPhaseWF.addMonitorValTime(new IEventSinkValTime() {

                    public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                        yPhase = newRecord.doubleArray();
                        phasePlotData.setPoints(x1, yPhase);
                        phasePlot.refreshGraphJPanel();
                    }
                }, Monitor.VALUE);
            } catch (ConnectionException e) {
                System.out.println("Cannot connect to " + bpmPhaseWF.getId());
            } catch (MonitorException e) {
                System.out.println("Cannot monitor " + bpmPhaseWF.getId());
            }
            try {
                ampArryMonitor = bpmAmpWF.addMonitorValTime(new IEventSinkValTime() {

                    public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                        yAmp = newRecord.doubleArray();
                        ampPlotData.setPoints(x1, yAmp);
                        ampPlot.refreshGraphJPanel();
                    }
                }, Monitor.VALUE);
            } catch (ConnectionException e) {
                System.out.println("Cannot connect to " + bpmAmpWF.getId());
            } catch (MonitorException e) {
                System.out.println("Cannot monitor " + bpmAmpWF.getId());
            }
        } else {
            Channel bpmXTBTWF = caF.getChannel(name + ":xTBT");
            Channel bpmYTBTWF = caF.getChannel(name + ":yTBT");
            try {
                double[] tmpArry = bpmXTBTWF.getArrDbl();
                x2 = new double[tmpArry.length];
            } catch (GetException ge) {
            } catch (ConnectionException ce) {
            }
            for (int i = 0; i < x2.length; i++) {
                x2[i] = i;
            }
            try {
                xTBTArryMonitor = bpmXTBTWF.addMonitorValTime(new IEventSinkValTime() {

                    public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                        yXTBT = newRecord.doubleArray();
                        xTBTPlotData.setPoints(x2, yXTBT);
                        xTBTPlot.refreshGraphJPanel();
                    }
                }, Monitor.VALUE);
            } catch (ConnectionException e) {
                System.out.println("Cannot connect to " + bpmXTBTWF.getId());
            } catch (MonitorException e) {
                System.out.println("Cannot monitor " + bpmXTBTWF.getId());
            }
            try {
                yTBTArryMonitor = bpmYTBTWF.addMonitorValTime(new IEventSinkValTime() {

                    public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                        yYTBT = newRecord.doubleArray();
                        yTBTPlotData.setPoints(x2, yYTBT);
                        yTBTPlot.refreshGraphJPanel();
                    }
                }, Monitor.VALUE);
            } catch (ConnectionException e) {
                System.out.println("Cannot connect to " + bpmYTBTWF.getId());
            } catch (MonitorException e) {
                System.out.println("Cannot monitor " + bpmYTBTWF.getId());
            }
        }
    }
