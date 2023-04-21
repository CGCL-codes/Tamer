    public void fadeValues(final CueValueSet cue, final short[] changedAddrs, final IChannelValueGetter valueGetter) throws IDMXDeviceException {
        final int sends = (int) (cue.getFadeUpMillis() / 22.0 + 0.5) + 1;
        final boolean[] threadTimerDone = new boolean[1];
        final String[] errorResult = new String[1];
        final boolean[] writeError = new boolean[1];
        threadTimerDone[0] = false;
        stopTransition[0] = false;
        writeError[0] = false;
        Timer threadTimer = new Timer();
        threadTimer.scheduleAtFixedRate(new TimerTask() {

            int send = 1;

            public void run() {
                cue.setFadeLevel(Utilities.clip(((float) (1.0 * send)) / sends));
                Channel[] channels = new Channel[changedAddrs.length];
                for (int i = 0; i < changedAddrs.length; i++) {
                    channels[i] = new Channel(changedAddrs[i], valueGetter.getChannelValue(changedAddrs[i]));
                }
                try {
                    setValues(channels);
                } catch (Exception e) {
                    threadTimerDone[0] = true;
                    writeError[0] = true;
                    errorResult[0] = e.getMessage();
                    cancel();
                    return;
                }
                if (stopTransition[0] == true) {
                    threadTimerDone[0] = true;
                    cancel();
                    return;
                }
                send++;
                if (send > sends) {
                    threadTimerDone[0] = true;
                    cancel();
                }
            }
        }, 0, 22);
        while (threadTimerDone[0] == false) Thread.yield();
        if (writeError[0] == true) throw new IDMXDeviceException("Error writting to the device! Error: " + errorResult[0]);
    }
