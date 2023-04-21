    public static void startAdjournedSimul2() {
        SimulHandler.getHandler().spoof(SimulHandler.getGiver().getHandle(), "startsimul");
        SimulHandler.setFirstGame(true);
        if (SimulHandler.getChannelOut()) {
            SimulHandler.getHandler().tell(SimulHandler.getChannel(), SimulHandler.getGiver().getDisplayHandle(false) + "'s simul has resumed! \"tell " + Settings.username + " follow\" to follow the action!");
        }
    }
