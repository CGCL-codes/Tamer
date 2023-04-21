    public MonitoredArrayPV() {
        updateListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                synchronized (syncObj) {
                    MonitoredPVEvent mpvEvt = (MonitoredPVEvent) e.getSource();
                    if (!switchOn) {
                        if (vals.length != 0) {
                            vals = new double[0];
                        }
                        return;
                    }
                    if (mpv != null && mpv.isGood()) {
                        ChannelRecord record = mpvEvt.getChannelRecord();
                        double[] localVals = new double[0];
                        if (record != null) {
                            localVals = record.doubleArray();
                        }
                        if (localVals.length != vals.length) {
                            vals = new double[localVals.length];
                        }
                        for (int i = 0; i < localVals.length; i++) {
                            vals[i] = localVals[i];
                        }
                    } else {
                        vals = new double[0];
                    }
                }
            }
        };
        mpv = MonitoredPV.getMonitoredPV("MonitoredArrayPV_" + nextIndex);
        mpv.addValueListener(updateListener);
        mpv.addStateListener(updateListener);
        nextIndex++;
    }
