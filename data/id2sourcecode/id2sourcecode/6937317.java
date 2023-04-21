    public void run() {
        dout.write("Starting thread(1) (thread,state)=" + State.str[state] + "," + Thread.currentThread().getName());
        boolean runAgain = true;
        while (runAgain) {
            if (coordinate() == MsgType.TERMINATED) {
                break;
            }
            if (state == State.I_SUCCESS) {
                enableInteraction();
            }
            state = State.I_MEETING;
            if (!repeat) {
                castMgr.resetRoles(State.R_UNAVAILABLE);
                runAgain = false;
            } else {
                castMgr.resetRoles(State.R_INIT);
            }
        }
        coordinator.notifyInteractionFinished();
        dout.write("notified finished");
        while (castMgr.getMessage() != MsgType.TERMINATED) ;
    }
