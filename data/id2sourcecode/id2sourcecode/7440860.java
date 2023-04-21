    private void handleHBTimer(HeartbeatTimer ev) {
        if (random.nextInt(100) < heartbeatThreshold) sendHeartbeat(ev.getChannel());
    }
