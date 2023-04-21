    private void playNextPitch() {
        int pitch = pitches[beat][sub_beat];
        JMIDI.getChannel(sub_beat).noteOn(pitch, 75);
        try {
            Thread.sleep(tempo / sub_meter);
        } catch (InterruptedException ignore) {
        }
        JMIDI.getChannel(sub_beat).noteOff(pitch);
        if (++sub_beat >= sub_meter) {
            sub_beat = 0;
            if (++beat >= meter) beat = 0;
        }
    }
