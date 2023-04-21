    private void makeChannel(MidiSequenceHandler sequence, TGChannel channel, int track) {
        if ((this.flags & ADD_MIXER_MESSAGES) != 0) {
            makeChannel(sequence, channel, track, true);
            if (channel.getChannel() != channel.getEffectChannel()) {
                makeChannel(sequence, channel, track, false);
            }
        }
    }
