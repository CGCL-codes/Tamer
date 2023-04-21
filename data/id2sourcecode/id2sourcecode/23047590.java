    private void append(final StringBuilder b, final ShortMessage message) {
        int command = message.getCommand();
        int channel = message.getChannel();
        if (command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF) {
            b.append("Note ");
            if (command == ShortMessage.NOTE_ON) {
                b.append("On");
            } else {
                b.append("Off");
            }
            b.append(" (channel=");
            b.append(channel);
            b.append(", note=");
            b.append(message.getData1());
            b.append(", velocity=");
            b.append(message.getData2());
            b.append(")");
        } else {
            String string = "Unknown Command";
            if (command == ShortMessage.CONTROL_CHANGE) {
                string = "Control Change";
            } else if (command == ShortMessage.POLY_PRESSURE) {
                string = "Polyphonic Key Pressure (Aftertouch)";
            } else if (command == ShortMessage.PROGRAM_CHANGE) {
                string = "Program Change";
            } else if (command == ShortMessage.CHANNEL_PRESSURE) {
                string = "Channel Pressure (Aftertouch)";
            } else if (command == ShortMessage.PITCH_BEND) {
                string = "Pitch Bend";
            }
            b.append(string);
            b.append(" (channel=");
            b.append(channel);
            b.append(", data1=");
            b.append(message.getData1());
            b.append(", data2=");
            b.append(message.getData2());
            b.append(")");
        }
    }
