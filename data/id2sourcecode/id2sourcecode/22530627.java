    public void ping(int note, LoxBall ball) {
        if (!RUNNING) return;
        int pingBeat = note / sub_meter, pingSubBeat = note % sub_meter;
        int pitch;
        long delay;
        if (ball.getState() == 0) {
            pitch = 33;
            delay = 50;
        } else {
            pitch = pitches[pingBeat][pingSubBeat] + (int) (24 / ((ball.getMaxState() - 1) / (float) ball.getState()));
            delay = 25;
        }
        JMIDI.getChannel(beat).noteOn(pitch, MAX_VOL);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignore) {
        }
        JMIDI.getChannel(beat).noteOff(pitch);
    }
