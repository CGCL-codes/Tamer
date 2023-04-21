    public static void vars(String handle) {
        Qtell qtell = new Qtell(handle);
        qtell.addLine("");
        qtell.addLine("Vars are currently set as follows:");
        qtell.addLine("Status is: " + Common.getStatus(SimulHandler.getStatus()));
        qtell.addLine("Current Manager: " + SimulHandler.getManager());
        qtell.addLine("Simul giver is: " + SimulHandler.getGiver().getDisplayHandle(false));
        qtell.addLine("Simul has no manager: " + SimulHandler.isNoManager());
        qtell.addLine("Print simul events to channel: " + SimulHandler.getChannelOut());
        qtell.addLine("Output channel is: " + SimulHandler.getChannel());
        qtell.addLine("Simul is rated: " + SimulHandler.isRated());
        qtell.addLine("Simul has random colors: " + SimulHandler.isRandColor());
        qtell.addLine("Simul giver has white: " + SimulHandler.isGiverWhite());
        qtell.addLine("Guests are allowed to play: " + SimulHandler.getAllowGuests());
        qtell.addLine("Computers are allowed to play: " + SimulHandler.getAllowComps());
        qtell.addLine("Maximum rating for joining is: " + SimulHandler.getMaxRating());
        qtell.addLine("Number of players allowed to join is: " + SimulHandler.getMaxPlayers());
        qtell.addLine("Intial time is: " + SimulHandler.getTime());
        qtell.addLine("Increment is: " + SimulHandler.getInc());
        qtell.addLine("Wild type is : " + SimulHandler.getWild());
        qtell.addLine("Message list of winners: " + SimulHandler.getMessWinners());
        qtell.addLine("Stored games are aborted: " + SimulHandler.isAutoAdjud());
        qtell.addLine("Lottery in effect: " + SimulHandler.isLottery());
        qtell.addLine("");
        qtell.send();
    }
