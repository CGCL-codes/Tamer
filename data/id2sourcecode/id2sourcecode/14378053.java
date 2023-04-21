    public static void setDatas(MidiMessage m, int data1, int data2) {
        if (!(m instanceof ShortMessage)) return;
        ShortMessage me = (ShortMessage) m;
        try {
            me.setMessage(me.getCommand(), me.getChannel(), data1, data2);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
