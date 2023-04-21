    public void swapPhaseSwitch() throws Exception {
        int currentSwitch = 0;
        if (!Double.isNaN(phaseSwitchRB.getValue())) {
            currentSwitch = (int) (phaseSwitchRB.getValue());
            if (phaseSwitchSet == null) {
                phaseSwitchSet = ChannelFactory.defaultFactory().getChannel(calib.getPhaseSwitchSetRecord());
            }
            int swapSwitch = (currentSwitch == 0) ? 1 : 0;
            System.out.println("swapping phaseswitch rec " + calib.getPhaseSwitchSetRecord());
            System.out.println("for FCT = " + FCTId);
            CaMonitorScalar.setChannel(phaseSwitchSet, swapSwitch);
        }
    }
