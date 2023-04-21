    public boolean checkVolt() {
        ArrayList<Double> buffer = voltMon.getVals();
        int lowCount = 0;
        int highCount = 0;
        for (int ib = 0; ib < buffer.size(); ib++) {
            double volt = buffer.get(ib).doubleValue();
            if (volt < voltLowThreshold) {
                lowCount++;
            }
            if (volt > voltHighThreshold) {
                highCount++;
            }
        }
        if ((lowCount > 0) && (highCount > 0)) {
            System.out.println("voltMon = " + voltMon.getChannelName());
            System.out.println("lowCount, highCount = " + lowCount + " " + highCount);
            isPhaseSwitchGood = false;
        } else {
            isPhaseSwitchGood = true;
        }
        return isPhaseSwitchGood;
    }
