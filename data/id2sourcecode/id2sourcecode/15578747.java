    public static void start2() {
        SimulHandler.getHandler().spoof(SimulHandler.getGiver().getHandle(), "startsimul", "Start the Simul");
        simulString += "startsimul";
        if (!AdminListener.isAdmin()) {
            SimulHandler.getHandler().qsuggest(SimulHandler.getGiver().getHandle(), simulString, "Start the simul.");
        }
        LatejoinTimer.start();
        FollowTimer.start();
        ElapsedTimer.start();
        SimulHandler.getHandler().tell(lastTeller, "The simul has started.");
        if (SimulHandler.getChannelOut()) {
            if (SimulHandler.getPlayers().size() < SimulHandler.getMaxPlayers()) {
                SimulHandler.getHandler().tell(SimulHandler.getChannel(), SimulHandler.getGiver().getDisplayHandle(false) + "'s simul has started, but there is still room for more players! \"tell " + Settings.username + " latejoin\" to join the action!");
            } else {
                SimulHandler.getHandler().tell(SimulHandler.getChannel(), SimulHandler.getGiver().getDisplayHandle(false) + "'s simul has started! \"tell " + Settings.username + " follow\" to follow the action!");
            }
        }
    }
