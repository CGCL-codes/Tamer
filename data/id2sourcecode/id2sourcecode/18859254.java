    public static void printInfo(String handle) {
        int status = SimulHandler.getStatus();
        Qtell qtell = new Qtell(handle);
        qtell.addLine("");
        qtell.addLine("Simul Information:");
        if (status == Settings.NO_SIMUL) {
            qtell.addLine("No current simul taking place.");
            qtell.addLine("" + ElapsedTimer.getMinutes() + " minutes have elapsed since the last simul.");
            qtell.addLine("There have been a total of " + RecordKeeper.getTotalSimuls() + " simuls run with " + Settings.username);
            qtell.addLine("Ask in channel " + SimulHandler.getChannel() + " for a simul.");
        } else if (status == Settings.SIMUL_OPEN) {
            qtell.addLine(SimulHandler.getGiver().getDisplayHandle(false) + " is opening a simul for " + SimulHandler.getMaxPlayers() + " players under " + SimulHandler.getMaxRating());
            qtell.addLine("Match string that will be issued to the simul giver: " + Common.buildMatch(false));
            qtell.addLine(SimulHandler.getPlayers().size() + " players have already joined. There will be a total of " + SimulHandler.getMaxPlayers() + " boards.");
            qtell.addLine("\"tell " + Settings.username + " players\" to see a list of players.");
            qtell.addLine("\"tell " + Settings.username + " join\" to be in!");
        } else if (status == Settings.SIMUL_RUNNING) {
            qtell.addLine(SimulHandler.getGiver().getDisplayHandle(false) + " is giving a simul to " + SimulHandler.getTotalPlayers() + " players.");
            qtell.addLine("Match string that was issued to the simul giver: " + Common.buildMatch(false));
            qtell.addLine("There are a total of " + SimulHandler.getSimulGames().size() + " games currently taking place.");
            qtell.addLine("Current results: wins: " + SimulHandler.getWins() + " draws: " + SimulHandler.getDraws() + " losses: " + SimulHandler.getLosses());
            qtell.addLine("\"tell " + Settings.username + " players\" to see a list of players.");
            qtell.addLine("\"tell " + Settings.username + " games\" to see a list of games.");
            qtell.addLine("\"tell " + Settings.username + " results\" to see current results.");
        } else if (status == Settings.GIVER_DISCONNECTED) {
            qtell.addLine("The simul giver has disconnected");
            qtell.addLine("Be patient - any adjourned simul games will be resumed automatically when the simul giver returns.");
        } else {
            qtell.addLine("Unknown state.");
        }
        qtell.addLine("");
        qtell.send();
    }
