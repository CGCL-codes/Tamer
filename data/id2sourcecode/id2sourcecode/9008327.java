    public void fadeValues(final CueValueSet startCue, final CueValueSet endCue, final short[] changedAddrs, final IChannelValueGetter valueGetter) throws ADMXDeviceException {
        final int fadeUpSends = (int) (endCue.getFadeUpMillis() / 22.0 + 0.5) + 1;
        final int fadeDownSends = (int) (endCue.getFadeDownMillis() / 22.0 + 0.5) + 1;
        final int totalSends = Math.max(fadeUpSends, fadeDownSends);
        final boolean[] threadTimerDone = new boolean[1];
        final boolean[] writeError = new boolean[1];
        writeError[0] = false;
        threadTimerDone[0] = false;
        stopTransition[0] = false;
        final String[] errorResult = new String[1];
        Timer threadTimer = new Timer();
        threadTimer.scheduleAtFixedRate(new TimerTask() {

            int send = 1;

            public void run() {
                startCue.setFadeLevel(clip((float) (1.0 * (fadeDownSends - send) / fadeDownSends)));
                endCue.setFadeLevel(clip(((float) (1.0 * send)) / fadeUpSends));
                for (short i = 0; i < 512; i++) {
                    myDmx[i] = (byte) (valueGetter.getChannelValue((short) (i + 1)));
                }
                try {
                    sendDmxPacket();
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
                if (send > totalSends) {
                    threadTimerDone[0] = true;
                    cancel();
                }
            }
        }, 0, 22);
        while (threadTimerDone[0] == false) Thread.yield();
        if (writeError[0] == true) throw new ADMXDeviceException("Error sending Art-Net Packet: " + errorResult[0]);
    }
