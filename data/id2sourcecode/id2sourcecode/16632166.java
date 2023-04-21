    @Override
    public void run() {
        initRun();
        for (int p = start_val; p < end_val; p++) {
            int w = JWondrous.wondrousness(p, limit, mult);
            current_pitch = playPitch(w);
            try {
                sleep(speed);
            } catch (InterruptedException augh) {
                return;
            }
        }
        JMIDI.getChannel(chan).allNotesOff();
        System.out.println("Channel " + chan + " done.");
    }
