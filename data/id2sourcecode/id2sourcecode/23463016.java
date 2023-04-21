    private static String prettyPrint(ShortMessage sm) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_OFF:
                return "Note #" + sm.getData1() + " OFF (ch=" + sm.getChannel() + ", vel=" + sm.getData2() + ")";
            case ShortMessage.NOTE_ON:
                return "Note #" + sm.getData1() + " ON (ch=" + sm.getChannel() + ", vel=" + sm.getData2() + ")";
            case ShortMessage.CONTROL_CHANGE:
                switch(sm.getData1()) {
                    case ControlChangeNumber.CHANNEL_VOLUME_LSB:
                        return "Channel " + sm.getChannel() + " volume LSB=" + sm.getData2();
                    case ControlChangeNumber.CHANNEL_VOLUME_MSB:
                        return "Channel " + sm.getChannel() + " volume MSB=" + sm.getData2();
                    case ControlChangeNumber.CHANNEL_BALANCE_LSB:
                        return "Channel " + sm.getChannel() + " balance LSB=" + sm.getData2();
                    case ControlChangeNumber.CHANNEL_BALANCE_MSB:
                        return "Channel " + sm.getChannel() + " balance MSB=" + sm.getData2();
                    default:
                        return "Control change no. " + sm.getData1() + " (ch=" + sm.getChannel() + ", val=" + sm.getData2() + ")";
                }
            default:
                return "Cmd: " + sm.getCommand() + " (ch=" + sm.getChannel() + ", data1=" + sm.getData1() + ", data2=" + sm.getData2() + ")";
        }
    }
