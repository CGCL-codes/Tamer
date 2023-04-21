    public static void save(Tell t) {
        int authorized = Common.isAuthorized(t);
        t.truncateFirstSpace();
        if (authorized < ACCESS_LEVEL) {
            SimulHandler.getHandler().tell(t.getHandle(), "You are not authorized to issue this command");
        } else {
            Properties p = new Properties();
            p.setProperty("nomanager", "" + SimulHandler.isNoManager());
            p.setProperty("channelout", "" + SimulHandler.getChannelOut());
            p.setProperty("channel", "" + SimulHandler.getChannel());
            p.setProperty("rated", "" + SimulHandler.isRated());
            p.setProperty("randcolor", "" + SimulHandler.isRandColor());
            p.setProperty("white", "" + SimulHandler.isGiverWhite());
            p.setProperty("allowguests", "" + SimulHandler.getAllowGuests());
            p.setProperty("allowcomps", "" + SimulHandler.getAllowComps());
            p.setProperty("maxrating", "" + SimulHandler.getMaxRating());
            p.setProperty("maxplayers", "" + SimulHandler.getMaxPlayers());
            p.setProperty("time", "" + SimulHandler.getTime());
            p.setProperty("inc", "" + SimulHandler.getInc());
            p.setProperty("wild", "" + SimulHandler.getWild());
            p.setProperty("messwinners", "" + SimulHandler.getMessWinners());
            p.setProperty("autoadjud", "" + SimulHandler.isAutoAdjud());
            p.setProperty("lottery", "" + SimulHandler.isLottery());
            String filePath = PREFERENCES_PATH + SimulHandler.getGiver().getHandle() + ".txt";
            Library.saveProperties(p, filePath, PREFERENCES_DESCRIPTION);
            SimulHandler.getHandler().tell(t.getHandle(), "Preferences for simul giver " + SimulHandler.getGiver().getDisplayHandle(false) + " have been saved successfully.");
        }
    }
