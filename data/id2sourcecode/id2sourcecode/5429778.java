    private void handleLeaveTimer(JGCSLeaveTimer timer) {
        if (leaveChannel != null) {
            try {
                new LeaveEvent(timer.getChannel(), Direction.DOWN, this, myGroup, vs.id).go();
                timer.setDir(Direction.invert(timer.getDir()));
                timer.setQualifierMode(EventQualifier.ON);
                timer.setSourceSession(this);
                timer.init();
                timer.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
    }
