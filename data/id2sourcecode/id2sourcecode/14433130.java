            public void run() {
                cue.setFadeLevel(clip(((float) (1.0 * send)) / sends));
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
                if (send > sends) {
                    threadTimerDone[0] = true;
                    cancel();
                }
            }
