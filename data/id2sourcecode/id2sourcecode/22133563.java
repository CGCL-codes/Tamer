    public void addControlChange(long tick, int track, int channelId, int controller, int value) {
        GMChannelRoute gmChannel = this.router.getRoute(channelId);
        if (gmChannel != null) {
            this.events.add(new MidiEvent(MidiMessageUtils.controlChange(gmChannel.getChannel1(), controller, value), tick));
            if (gmChannel.getChannel1() != gmChannel.getChannel2()) {
                this.events.add(new MidiEvent(MidiMessageUtils.controlChange(gmChannel.getChannel2(), controller, value), tick));
            }
        }
    }
