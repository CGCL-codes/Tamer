    private void handleJGCSJoin(JGCSJoinEvent event) {
        requestedJoin = true;
        jgcsGroupName = event.getGroupName();
        sendGroupInit(event.getChannel());
    }
