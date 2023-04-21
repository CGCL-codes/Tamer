    public static void printFinalResults() {
        double totalGames = (double) SimulHandler.getWins() + SimulHandler.getDraws() + SimulHandler.getLosses();
        Qtell qtell = new Qtell(SimulHandler.getChannel());
        qtell.addLine("");
        qtell.addLine("Results for " + SimulHandler.getGiver().getDisplayHandle(false) + "'s simul: ");
        qtell.addLine("wins: " + SimulHandler.getWins() + " (" + Common.roundDouble(100 * SimulHandler.getWins() / totalGames) + "%)");
        qtell.addLine("draws: " + SimulHandler.getDraws() + " (" + Common.roundDouble(100 * SimulHandler.getDraws() / totalGames) + "%)");
        ;
        qtell.addLine("losses: " + SimulHandler.getLosses() + " (" + Common.roundDouble(100 * SimulHandler.getLosses() / totalGames) + "%)");
        qtell.addLine("");
        qtell.addLine("Congratulations to the (over-the-board) winners:");
        qtell.send();
        for (Player p : SimulHandler.getWinners()) {
            qtell.add(p.getDisplayHandle(false));
        }
        qtell.addLine("");
        qtell.addLine("");
        qtell.send();
    }
