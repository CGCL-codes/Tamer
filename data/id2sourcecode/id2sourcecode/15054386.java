            @Override
            public void run() {
                Latejoin.setLateJoinAllowed(false);
                if (SimulHandler.getChannelOut()) {
                    SimulHandler.getHandler().qaddevent(Common.buildFollowEventString());
                }
            }
