    public MidiChannelWriter getChannelWriter(int chan) {
        if (encoders[chan] == null) {
            encoders[chan] = new DefaultMidiChannelWriter(this, chan);
        }
        return encoders[chan];
    }
