    public static MidiChannel getChannel(int chan) {
        if (isReady()) return channels[chan]; else return null;
    }
