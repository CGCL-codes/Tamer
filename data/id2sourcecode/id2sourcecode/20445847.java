    public float getVolume() {
        if (sequencer instanceof Synthesizer == false) {
            return volume;
        }
        MidiChannel[] channels = ((Synthesizer) sequencer).getChannels();
        return ((float) channels[0].getController(GAIN_CONTROLLER)) / 127;
    }
