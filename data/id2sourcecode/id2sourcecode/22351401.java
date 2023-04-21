    public static void closeSimul() {
        Open.closeAdvertising();
        clearTourneyStuff();
        if (SimulHandler.getStatus() == Settings.SIMUL_OPEN) {
            String message = "The current simul has been closed. Please watch channel " + SimulHandler.getChannel() + " or ask the simul giver for more information.";
            Common.tellAllPlayers(message);
        }
        if (SimulHandler.getChannelOut()) {
            String message = "**** " + SimulHandler.getGiver().getDisplayHandle(false) + "'s simul has been closed. ****";
            SimulHandler.getHandler().tell(SimulHandler.getChannel(), message);
        }
        Common.resetSimul();
    }
