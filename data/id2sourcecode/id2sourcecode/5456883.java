    public static String midiMessageToString(MidiMessage mm) {
        String r = "(unknown)";
        if (mm instanceof MetaMessage) {
            MetaMessage m = (MetaMessage) mm;
            r = "MetaMessage:" + m.getType();
        } else if (mm instanceof ShortMessage) {
            ShortMessage m = (ShortMessage) mm;
            r = "c" + m.getChannel() + ":" + statusToString(m.getCommand()) + ":" + m.getData1() + ":" + m.getData2();
        } else if (mm instanceof SysexMessage) {
            r = "SysexMessage";
        }
        return r;
    }
