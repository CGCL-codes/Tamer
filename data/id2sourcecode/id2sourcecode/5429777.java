    private void handleJGCSLeave(JGCSLeaveEvent event) {
        leaveChannel = event.getLatch();
        if (vs == null) {
            sentGroupInit = true;
            leaveChannel.countDown();
        } else if (!isBlocked) sendLeave(event.getChannel()); else requestedLeave = true;
    }
