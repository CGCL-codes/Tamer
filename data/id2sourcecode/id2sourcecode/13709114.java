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
