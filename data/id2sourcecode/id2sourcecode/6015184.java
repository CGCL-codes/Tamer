    public static int getChannel(MidiMessage msg) {
        return getMessage(msg)[2] & 0x7F;
    }
