    public static void printResultsInProgress(Game g) {
        Qtell qtell = new Qtell(SimulHandler.getChannel());
        qtell.addLine(g.getWhite().getDisplayHandle(true) + " vs " + g.getBlack().getDisplayHandle(true) + " : " + g.getResult());
        qtell.addLine(SimulHandler.getGiver().getDisplayHandle(false) + "'s simul: " + SimulHandler.getWins() + " win" + (SimulHandler.getWins() == 1 ? "" : "s") + ", " + SimulHandler.getDraws() + " draw" + (SimulHandler.getDraws() == 1 ? "" : "s") + ", " + SimulHandler.getLosses() + " loss" + (SimulHandler.getLosses() == 1 ? "" : "es") + ", and " + (SimulHandler.getSimulGames().size() + SimulHandler.getPlayersWithAdjournedGames().size()) + " remaining.");
        qtell.send();
    }
