    public ArrayDataPV() {
        updateListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                synchronized (syncObj) {
                    if (!switchOn) {
                        if (vals.length != 0) {
                            vals = new double[0];
                        }
                        return;
                    }
                    if (mpv != null && mpv.isGood()) {
                        double[] localVals = new double[0];
                        Channel ch = mpv.getChannel();
                        try {
                            localVals = ch.getArrDbl();
                        } catch (ConnectionException exp) {
                            vals = new double[0];
                        } catch (GetException exp) {
                            vals = new double[0];
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
                update();
            }
        };
        mpv = MonitoredPV.getMonitoredPV("ArrayDataPV_" + nextIndex);
        mpv.addValueListener(updateListener);
        nextIndex++;
    }
