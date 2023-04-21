    public static void remove(String player) {
        SimulHandler.setTotal(SimulHandler.getTotal() - 1);
        Player p = Common.findPlayer(player);
        removeByHandle(player, false);
        if (SimulHandler.getChannelOut()) {
            Qtell qtell = new Qtell(SimulHandler.getChannel());
            qtell.addLine(p.getDisplayHandle(false) + " has left " + SimulHandler.getGiver().getDisplayHandle(false) + "'s simul - " + Common.buildRemainingString(false));
            qtell.send();
            SimulHandler.getHandler().qremoveevent(Settings.EVENT_TYPE);
            SimulHandler.getHandler().qaddevent(Common.buildEventString(false));
        }
    }
