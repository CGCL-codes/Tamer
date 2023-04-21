    public static void addPlayer(Player p) {
        SimulHandler.getPlayers().add(p);
        if (Common.followingSimul(p.getHandle())) {
            SimulHandler.getFollowers().remove(Common.findFollower(p.getHandle()));
        }
        SimulHandler.getHandler().qsetTourney(p.getHandle(), Common.buildMatchSettings(false, false), !SimulHandler.isGiverWhite());
        SimulHandler.getHandler().qchanplus(p.getHandle(), SimulHandler.getChannel());
        SimulHandler.getHandler().tell(p.getHandle(), "You have joined the simul.");
        SimulHandler.getRecorder().record(p.getHandle() + " has joined the simul");
        Qtell qtell = new Qtell(p.getHandle());
        String line;
        qtell.addLine("");
        qtell.addLine(createCopies(MOTD_LINE_SIZE, "*"));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MOTD_PATH));
            while ((line = reader.readLine()) != null) {
                String tempSpacer = createCopies(MOTD_LINE_SIZE - line.length() - 4, " ");
                qtell.addLine("* " + line + tempSpacer + " *");
            }
        } catch (Exception e) {
            SimulHandler.getRecorder().record("Error reading MOTD: " + e);
        }
        qtell.addLine(createCopies(MOTD_LINE_SIZE, "*"));
        qtell.send();
        int remainingPlayers = SimulHandler.getMaxPlayers() - SimulHandler.getPlayers().size();
        if (SimulHandler.getChannelOut()) {
            qtell = new Qtell(SimulHandler.getChannel());
            qtell.addLine(p.getDisplayHandle(false) + "(" + p.getRating() + ") has joined " + SimulHandler.getGiver().getDisplayHandle(false) + "'s simul - " + Common.buildRemainingString(false));
            if (!SimulHandler.isLottery() && remainingPlayers == 0) {
                qtell.addLine("Simul is now full.");
                Open.disableAdvertising();
            } else if (!SimulHandler.isLottery() && remainingPlayers == 1) {
                Open.enableAdvertising();
            }
            qtell.send();
        }
        SimulHandler.setTotal(SimulHandler.getTotal() + 1);
        SimulHandler.getHandler().plusNotify(p.getHandle());
    }
