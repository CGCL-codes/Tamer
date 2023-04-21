    private void startMonitors(String name) {
        ChannelFactory caF = ChannelFactory.defaultFactory();
        Channel bcmWF = caF.getChannel(name + ":currentTBT");
        try {
            double[] tmpArry = bcmWF.getArrDbl();
            x1 = new double[tmpArry.length];
        } catch (GetException ge) {
        } catch (ConnectionException ce) {
        }
        for (int i = 0; i < x1.length; i++) {
            x1[i] = i;
        }
        try {
            bcmWFMonitor = bcmWF.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    yBCMWF = newRecord.doubleArray();
                    bcmPlotData.setPoints(x1, yBCMWF);
                    bcmPlot.refreshGraphJPanel();
                }
            }, Monitor.VALUE);
        } catch (ConnectionException e) {
            System.out.println("Cannot connect to " + bcmWF.getId());
        } catch (MonitorException e) {
            System.out.println("Cannot monitor " + bcmWF.getId());
        }
    }
