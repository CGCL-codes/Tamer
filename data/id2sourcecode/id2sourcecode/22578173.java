    @Override
    public void run() {
        allsclcavs = allCavs.size();
        if (allsclcavs > 0) {
            try {
                affm = new AffMeasure[allsclcavs];
                String[] err = new String[allsclcavs];
                for (int i = 0; i < allsclcavs; i++) {
                    err[i] = ((RfCavity) allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "PeakErr";
                    affm[i] = new AffMeasure(myDoc);
                    affm[i].row = i;
                    if (affm[i].setPV1(err[i])) {
                        Thread nm = new Thread(affm[i]);
                        nm.start();
                    }
                }
            } catch (NullPointerException ne) {
            }
        }
    }
