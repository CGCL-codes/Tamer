    public MidiChannelWriter getChannelWriter(int chan) {
        if (encoders[chan] == null) {
            encoders[chan] = createChannelWriter(chan);
        }
        return encoders[chan];
    }
