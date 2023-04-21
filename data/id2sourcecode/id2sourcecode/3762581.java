    private boolean reJoin(String channel) {
        String[][] chans = getChannels();
        for (int i = 0; i < chans.length; i++) {
            if (chans[i][0] != null && chans[i][0].equals(channel)) {
                try {
                    Thread.sleep(timeToSleep);
                    parent.joinChannel(chans[i][0], chans[i][1]);
                    if (verbose) parent.log("[AutoJoin] rejoining channel " + chans[i]);
                    return true;
                } catch (InterruptedException e) {
                    if (verbose) parent.log("[AutoJoin] could not rejoin channel (could not sleep), aborting.");
                    return false;
                }
            }
        }
        return false;
    }
