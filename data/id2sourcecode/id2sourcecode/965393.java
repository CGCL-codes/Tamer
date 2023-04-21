    private void measure(MonitoredPVEvent mpvEvnt) {
        if (!listenToEPICS) {
            return;
        }
        double avgValue = 0.;
        double diffValue = 0.;
        ChannelRecord record = mpvEvnt.getChannelRecord();
        if (record != null) {
            double[] arr = record.doubleArray();
            int i_min = startAvgInd;
            int i_max = Math.min(arr.length, stopAvgInd);
            double s = 0;
            int count = 0;
            for (int i = i_min; i < i_max; i++) {
                if (Math.abs(arr[i]) > 1.0e-10) {
                    s += arr[i];
                    count++;
                }
            }
            if (count > 0) {
                s /= count;
            }
            avgValue = s;
            if (arr.length != y_arr.length) {
                y_arr = new double[arr.length];
                x_arr = new double[arr.length];
            }
            for (int i = 0; i < arr.length; i++) {
                y_arr[i] = arr[i];
                x_arr[i] = i;
            }
            if (ringBPMtsDiff.getPlusIndex() < arr.length && ringBPMtsDiff.getMinusIndex() < arr.length) {
                diffValue = y_arr[ringBPMtsDiff.getPlusIndex()] - y_arr[ringBPMtsDiff.getMinusIndex()];
            }
        } else {
            y_arr = new double[0];
            x_arr = new double[0];
        }
        dataStack.add(new Double(avgValue));
        ringBPMtsDiff.addValue(diffValue);
        if (dataStack.size() > stackCapacity) {
            for (int i = 0, n = dataStack.size() - stackCapacity; i < n; i++) {
                Object obj = dataStack.firstElement();
                dataStack.removeElement(obj);
            }
        }
        uc.update();
    }
