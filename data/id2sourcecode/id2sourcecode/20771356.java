    public static void finishSimul() {
        SimulHandler.getHandler().qremoveevent(Settings.EVENT_TYPE);
        FollowTimer.cancel();
        ElapsedTimer.cancel();
        if (SimulHandler.isAutoAdjud()) {
            Common.clearAdjournedGames();
        }
        for (Player p : SimulHandler.getPlayers()) {
            SimulHandler.getHandler().qclear(p.getHandle());
        }
        if (SimulHandler.getMessWinners()) {
            Common.messageWinnersList();
        }
        RecordKeeper.incSimulsManaged(SimulHandler.getManager());
        History.addSimul(Library.getDate() + SimulHandler.getGiver().getDisplayHandle(false) + " +" + SimulHandler.getWins() + " =" + SimulHandler.getDraws() + " -" + SimulHandler.getLosses() + "\n");
        if (SimulHandler.isLottery()) {
            RecordKeeper.incTotalSimuls();
        }
        while (SimulHandler.getPlayersWithAdjournedGames().size() != 0) {
            SimulHandler.setTotal(SimulHandler.getTotal() - 1);
            Remove.removeByHandle(SimulHandler.getPlayersWithAdjournedGames().get(0).getHandle(), true);
        }
        SimulHandler.getHandler().tell(SimulHandler.getGiver().getHandle(), "You have finished the simul!");
        if (SimulHandler.getChannelOut()) {
            Results.printFinalResults();
        }
        Common.resetSimul();
    }
