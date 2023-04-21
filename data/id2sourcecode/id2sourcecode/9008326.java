            public void run() {
                cue.setFadeLevel(clip(((float) (1.0 * send)) / sends));
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
                if (send > sends) {
                    threadTimerDone[0] = true;
                    cancel();
                }
            }
