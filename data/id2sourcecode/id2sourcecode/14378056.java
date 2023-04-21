    public static void setCommand(MidiMessage m, int command) {
        if (!(m instanceof ShortMessage)) return;
        ShortMessage me = (ShortMessage) m;
        try {
            me.setMessage(command, me.getChannel(), me.getData1(), me.getData2());
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
