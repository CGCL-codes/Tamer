    private static String getChannelMessage(int statusByte, int value1) {
        switch(statusByte / 16) {
            case 8:
                return "NOTE_OFF";
            case 9:
                return "NOTE_ON";
            case 10:
                return "POLY_PRESSURE";
            case 11:
                if (value1 >= 120) {
                    return "CHANNEL_MODE_MESSAGE";
                } else {
                    return "CONTROL_CHANGE";
                }
            case 12:
                return "PROGRAM_CHANGE";
            case 13:
                return "CHANNEL_PRESSURE";
            case 14:
                return "PITCH_BEND_CHANGE";
            default:
                return String.valueOf(statusByte);
        }
    }
