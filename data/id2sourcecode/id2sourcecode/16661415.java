    public static String eventToString(MidiEvent event) {
        ShortMessage msg = null;
        if (event.getMessage() instanceof ShortMessage) {
            msg = (ShortMessage) event.getMessage();
        } else return null;
        int volume = -1;
        switch(msg.getCommand()) {
            case ShortMessage.NOTE_ON:
                volume = msg.getData2();
                break;
            case ShortMessage.NOTE_OFF:
                volume = 0;
                break;
            case ShortMessage.CONTROL_CHANGE:
                return "control " + msg.getChannel() + " " + msg.getData1() + " " + msg.getData2() + " " + event.getTick() + "\n";
            default:
                return null;
        }
        int height = msg.getData1();
        String s = "note " + msg.getChannel() + " " + height + " " + volume + " " + event.getTick() + "\n";
        return s;
    }
