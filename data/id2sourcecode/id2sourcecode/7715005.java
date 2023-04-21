    public static int getChannel(MidiMessage msg) {
        return ((ShortMessage) msg).getChannel();
    }
