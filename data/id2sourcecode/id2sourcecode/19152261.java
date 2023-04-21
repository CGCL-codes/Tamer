    private void reconnect() {
        if (attempts++ >= max_attempts) return;
        try {
            Thread.sleep(timeToSleep);
            if (verbose) log("re-connecting to '" + parent.getServer() + "'");
            parent.connect();
            if (parent.isConnected()) {
                attempts = 0;
                for (int i = 0; i < getChannels().length; i++) parent.joinChannel(getChannels()[i][0], getChannels()[i][1]);
            } else {
                reconnect();
            }
        } catch (InterruptedException e) {
            if (verbose) log("could not reconnect: " + e.getMessage());
        }
    }
