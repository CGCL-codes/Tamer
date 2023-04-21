    public static synchronized void start() {
        if (task != null) {
            cancel();
        }
        Latejoin.setLateJoinAllowed(true);
        task = new TimerTask() {

            @Override
            public void run() {
                Latejoin.setLateJoinAllowed(false);
                if (SimulHandler.getChannelOut()) {
                    SimulHandler.getHandler().qaddevent(Common.buildFollowEventString());
                }
            }
        };
        timer.schedule(task, LATEJOIN_PERIOD);
    }
