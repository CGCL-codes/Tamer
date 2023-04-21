    private void measure(MonitoredPVEvent mpvEvnt) {
        if (!listenToEPICS) {
            return;
        }
        double avgValue = 0.;
        ChannelRecord record = mpvEvnt.getChannelRecord();
        if (record != null) {
            avgValue = record.doubleValue();
        }
        dataStack.add(new Double(avgValue));
        if (dataStack.size() > stackCapacity) {
            for (int i = 0, n = dataStack.size() - stackCapacity; i < n; i++) {
                Object obj = dataStack.firstElement();
                dataStack.removeElement(obj);
            }
        }
        uc.update();
    }
