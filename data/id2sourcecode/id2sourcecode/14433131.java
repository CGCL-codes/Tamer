    public void fadeValues(final CueValueSet startCue, final CueValueSet endCue, final short[] changedAddrs, final IChannelValueGetter valueGetter) throws ADMXDeviceException {
        final byte[] data = new byte[changedAddrs.length * 3];
        final int fadeUpSends = (int) (endCue.getFadeUpMillis() / 22.0 + 0.5) + 1;
        final int fadeDownSends = (int) (endCue.getFadeDownMillis() / 22.0 + 0.5) + 1;
        final int totalSends = Math.max(fadeUpSends, fadeDownSends);
        final boolean[] threadTimerDone = new boolean[1];
        final boolean[] writeError = new boolean[1];
        writeError[0] = false;
        threadTimerDone[0] = false;
        stopTransition[0] = false;
        for (int i = 0; i < changedAddrs.length * 3; i += 3) {
            if (changedAddrs[i / 3] <= 256) {
                data[i] = 0x48;
                data[i + 1] = (byte) (changedAddrs[i / 3] - 1);
            } else if (changedAddrs[i / 3] <= 512) {
                data[i] = 0x49;
                data[i + 1] = (byte) (changedAddrs[i / 3] - 1);
            }
        }
        final long[] errorResult = new long[1];
        Timer threadTimer = new Timer();
        threadTimer.scheduleAtFixedRate(new TimerTask() {

            int send = 1;

            public void run() {
                startCue.setFadeLevel(clip((float) (1.0 * (fadeDownSends - send) / fadeDownSends)));
                endCue.setFadeLevel(clip(((float) (1.0 * send)) / fadeUpSends));
                for (int i = 0; i < changedAddrs.length * 3; i += 3) {
                    data[i + 2] = (byte) (valueGetter.getChannelValue(changedAddrs[i / 3]));
                }
                long result = Write(handle, data, changedAddrs.length * 3);
                if (result != 0L) {
                    threadTimerDone[0] = true;
                    writeError[0] = true;
                    errorResult[0] = result;
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
        if (writeError[0] == true) throw new ADMXDeviceException("Error writting to USBDMX.com device! Error code: " + Long.toString(errorResult[0]));
    }
