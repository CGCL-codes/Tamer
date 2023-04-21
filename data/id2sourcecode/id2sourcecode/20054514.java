        public void run() {
            MidiChannel channel = model.getSynthesizer().getChannels()[9];
            while (run) {
                try {
                    if (timeToSleepOnStart > 0) {
                        sleep(timeToSleepOnStart);
                        timeToSleepOnStart = 0;
                    }
                    if (mode == Mode.ALWAYS || mode == Mode.REC_PLAY && (model.getSequencer().isRecording() || model.getSequencer().isRunning()) || mode == Mode.REC && model.getSequencer().isRecording()) {
                        if (beatCounter == 0) {
                            channel.noteOn(ACCENTED_NOTE_NUMBER, VELOCITY);
                        } else {
                            channel.noteOn(NORMAL_NOTE_NUMBER, VELOCITY);
                        }
                    }
                    wentToSleepAt = System.currentTimeMillis();
                    timeUntilNextBeat = (long) (model.getSequencer().getTempoInMPQ() * 0.004 / timeSignature.getDenominator());
                    sleep(timeUntilNextBeat);
                    beatCounter = (beatCounter + 1) % timeSignature.getNumerator();
                } catch (InterruptedException e) {
                }
            }
        }
