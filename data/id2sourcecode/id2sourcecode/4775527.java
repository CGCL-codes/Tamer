    public static void addLatePlayer(Player p) {
        String handle = p.getHandle();
        if (Common.inSimul(handle)) {
            SimulHandler.getHandler().tell(handle, "You cannot latejoin the simul because you are already in.");
            return;
        }
        SimulHandler.getPlayers().add(new Player(GetpxListener.getUser(), GetpxListener.getTitle(), GetpxListener.isLoggedOn(), GetpxListener.isPlaying(), GetpxListener.getRating()));
        SimulHandler.setTotal(SimulHandler.getTotal() + 1);
        SimulHandler.getHandler().plusNotify(handle);
        SimulHandler.getHandler().qchanplus(handle, SimulHandler.getChannel());
        if (SimulHandler.getChannelOut()) {
            if (SimulHandler.getPlayers().size() + SimulHandler.getFinishedPlayers().size() == SimulHandler.getMaxPlayers()) {
                SimulHandler.getHandler().qaddevent(Common.buildFollowEventString());
            }
        }
        SimulHandler.getHandler().tell(handle, "You have been added to the simul. Your game will start in " + START_DELAY_TIME + " seconds.");
        SimulHandler.getRecorder().record(handle + " has latejoined the simul.");
        LateStartTimer.start(handle, START_DELAY_TIME);
    }
