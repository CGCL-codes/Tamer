    private int playPitch(int val) {
        int new_pitch = (int) MiscUtil.mapValueToRange(val, 0, limit, pitch_base, pitch_base + pitch_range);
        if (new_pitch != current_pitch) {
            JMIDI.getChannel(chan).noteOff(current_pitch);
            JMIDI.getChannel(chan).noteOn(new_pitch, vol);
        }
        return new_pitch;
    }
