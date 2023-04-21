    private static String getShortMessageString(ShortMessage shortMessage) {
        int channel = shortMessage.getChannel();
        int command = shortMessage.getCommand();
        int data1 = shortMessage.getData1();
        int data2 = shortMessage.getData2();
        int status = shortMessage.getStatus();
        String statusString = "UNKNOWN", commandString = "UNKNOWN", data1String = "UNKNOWN", data2String = "UNKNOWN";
        if (status == ShortMessage.ACTIVE_SENSING) statusString = "ACTIVE_SENSING";
        if (command == ShortMessage.CHANNEL_PRESSURE) commandString = "CHANNEL_PRESSURE";
        if (status == ShortMessage.CONTINUE) statusString = "CONTINUE";
        if (command == ShortMessage.CONTROL_CHANGE) commandString = "CONTROL_CHANGE";
        if (status == ShortMessage.END_OF_EXCLUSIVE) statusString = "END_OF_EXCLUSIVE";
        if (status == ShortMessage.MIDI_TIME_CODE) statusString = "MIDI_TIME_CODE";
        if (command == ShortMessage.NOTE_OFF) {
            commandString = "NOTE_OFF";
            data1String = "PITCH=" + data1;
            data2String = "VEL=" + data2;
        }
        if (command == ShortMessage.NOTE_ON) {
            commandString = "NOTE_ON";
            data1String = "PITCH=" + data1;
            data2String = "VEL=" + data2;
        }
        if (command == ShortMessage.PITCH_BEND) commandString = "PITCH_BEND";
        if (command == ShortMessage.POLY_PRESSURE) commandString = "POLY_PRESSURE";
        if (command == ShortMessage.PROGRAM_CHANGE) commandString = "PROGRAM_CHANGE";
        if (status == ShortMessage.SONG_POSITION_POINTER) statusString = "SONG_POSITION_POINTER";
        if (status == ShortMessage.SONG_SELECT) statusString = "SONG_SELECT";
        if (status == ShortMessage.START) statusString = "START";
        if (status == ShortMessage.STOP) statusString = "STOP";
        if (status == ShortMessage.SYSTEM_RESET) statusString = "SYSTEM_RESET";
        if (status == ShortMessage.TIMING_CLOCK) statusString = "TIMING_CLOCK";
        if (status == ShortMessage.TUNE_REQUEST) statusString = "TUNE_REQUEST";
        return "ShortMessage, " + "channel: " + channel + ", " + "command: " + commandString + " (" + command + "), " + "status: " + statusString + " (" + status + "), " + "data1: " + data1String + " (" + data1 + "), " + "data2: " + data2String + " (" + data2 + ")";
    }
