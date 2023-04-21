    public static void handleGetpxForGiver() {
        if (!GetpxListener.isExisting()) {
            SimulHandler.getHandler().tell(lastTeller, GetpxListener.getUser() + " could not be set as the simul giver because he or she doesn't exist");
        } else {
            SimulHandler.setGiver(new Player(GetpxListener.getUser(), GetpxListener.getTitle(), GetpxListener.isLoggedOn(), GetpxListener.isPlaying(), GetpxListener.getRating()));
            SimulHandler.getHandler().qtell(lastTeller, "Var \"giver\" was set to \"" + SimulHandler.getGiver().getHandle() + "\".");
            if (SimulHandler.getStatus() == Settings.SIMUL_OPEN && SimulHandler.getChannelOut()) {
                Qtell qtell = new Qtell(SimulHandler.getChannel());
                qtell.addLine("Var \"giver\" was set to \"" + SimulHandler.getGiver().getHandle() + "\".");
                qtell.send();
            }
            if (!SimulHandler.getGiver().isLoggedOn()) {
                SimulHandler.getHandler().tell(lastTeller, "Simul giver \"" + SimulHandler.getGiver().getHandle() + "\" is not currently logged on.");
            } else if (SimulHandler.getStatus() == Settings.SIMUL_OPEN) {
                SimulHandler.getHandler().tell(SimulHandler.getGiver().getHandle(), "You have been set as the simul giver.");
            }
        }
    }
