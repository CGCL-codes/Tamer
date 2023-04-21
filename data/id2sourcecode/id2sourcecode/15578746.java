    public static void startSimul() {
        SimulHandler.setStatus(Settings.SIMUL_RUNNING);
        Open.closeAdvertising();
        if (SimulHandler.getPlayers().size() > SimulHandler.getMaxPlayers() && SimulHandler.isLottery()) {
            runLottery();
        }
        if (SimulHandler.getChannelOut()) {
            if (SimulHandler.getPlayers().size() == SimulHandler.getMaxPlayers()) {
                SimulHandler.getHandler().qaddevent(Common.buildFollowEventString());
            } else {
                SimulHandler.getHandler().qaddevent(Common.buildEventString(true));
            }
        }
        String setVars = "multi set open 1; set ropen 1; set wopen 1; set useformula 0; set noescape 0";
        SimulHandler.getHandler().spoof(SimulHandler.getGiver().getHandle(), setVars, "Enable Match Requests");
        for (Player p : SimulHandler.getPlayers()) {
            if (SimulHandler.isRandColor()) {
                SimulHandler.setGiverWhite(Library.randomBoolean());
            }
            SimulHandler.getHandler().qsetTourney(p.getHandle(), Common.buildMatchSettings(true, false), !SimulHandler.isGiverWhite());
            SimulHandler.getHandler().qmatch(p.getHandle(), SimulHandler.getGiver().getHandle());
            sleep(10);
        }
        simulString = "multi ";
        for (Player p : SimulHandler.getPlayers()) {
            simulString += "+simul " + p.getHandle() + "; ";
            SimulHandler.getHandler().spoof(SimulHandler.getGiver().getHandle(), "+simul " + p.getHandle());
            sleep(10);
        }
        SimulHandler.getHandler().spoof(SimulHandler.getGiver().getHandle(), "tell " + Settings.username + " feedback " + Settings.PASS_TO_START_2);
        if (!AdminListener.isAdmin()) {
            SimulHandler.getHandler().qsuggest(SimulHandler.getGiver().getHandle(), "tell " + Settings.username + " feedback " + Settings.PASS_TO_START_2, "Confirm starts");
        }
    }
