    public static void updateResults(Game g) {
        if (g.getWhite().getHandle().equalsIgnoreCase((SimulHandler.getGiver().getHandle()))) {
            if (g.getResult().equals("1-0")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setWins(SimulHandler.getWins() + 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
                SimulHandler.getRecorder().record("Finished removing game " + g.getGameNumber());
            } else if (g.getResult().equals("0-1")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setLosses(SimulHandler.getLosses() + 1);
                if (g.isValidWin()) {
                    SimulHandler.getWinners().add(g.getBlack());
                }
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("1/2-1/2")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setDraws(SimulHandler.getDraws() + 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("aborted")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setTotal(SimulHandler.getTotal() - 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("*")) {
                SimulHandler.getRecorder().record("Seeing a adjourned game...");
                SimulHandler.getSimulGames().remove(g);
            } else {
                SimulHandler.getRecorder().record("Error updating results - invalid game result");
            }
        } else {
            if (g.getResult().equals("1-0")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setLosses(SimulHandler.getLosses() + 1);
                if (g.isValidWin()) {
                    SimulHandler.getWinners().add(g.getWhite());
                }
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("0-1")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setWins(SimulHandler.getWins() + 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("1/2-1/2")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setDraws(SimulHandler.getDraws() + 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("aborted")) {
                Remove.removeByGame(g);
                SimulHandler.getSimulGames().remove(g);
                SimulHandler.setTotal(SimulHandler.getTotal() - 1);
                if (!checkForFinish() && SimulHandler.getChannelOut()) {
                    Results.printResultsInProgress(g);
                }
            } else if (g.getResult().equals("*")) {
                SimulHandler.getRecorder().record("Seeing a adjourned game...");
                SimulHandler.getSimulGames().remove(g);
            } else {
                SimulHandler.getRecorder().record("Error updating results - invalid game result");
            }
        }
    }
