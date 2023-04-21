    public static void handleGetpxForManager() {
        if (!GetpxListener.isExisting()) {
            SimulHandler.getHandler().tell(lastTeller, GetpxListener.getUser() + " could not be set as the simul manager because he or she doesn't exist");
        } else {
            SimulHandler.setManager(GetpxListener.getUser().toLowerCase());
            SimulHandler.getHandler().qtell(lastTeller, "Var \"manager\" was set to \"" + SimulHandler.getManager() + "\".");
            if (SimulHandler.getStatus() == Settings.SIMUL_OPEN && SimulHandler.getChannelOut()) {
                Qtell qtell = new Qtell(SimulHandler.getChannel());
                qtell.addLine("Var \"manager\" was set to \"" + SimulHandler.getManager() + "\".");
                qtell.send();
            }
            if (!GetpxListener.isLoggedOn()) {
                SimulHandler.getHandler().tell(lastTeller, "Simul manager \"" + SimulHandler.getManager() + "\" is not currently logged on.");
            } else {
                SimulHandler.getHandler().tell(SimulHandler.getManager(), "You have been set as the simul manager.");
            }
        }
    }
