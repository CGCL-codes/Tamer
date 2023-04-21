    public void addProgramChange(long tick, int track, int channelId, int instrument) {
        GMChannelRoute gmChannel = this.router.getRoute(channelId);
        if (gmChannel != null) {
            this.events.add(new MidiEvent(MidiMessageUtils.programChange(gmChannel.getChannel1(), instrument), tick));
            if (gmChannel.getChannel1() != gmChannel.getChannel2()) {
                this.events.add(new MidiEvent(MidiMessageUtils.programChange(gmChannel.getChannel2(), instrument), tick));
            }
        }
    }
