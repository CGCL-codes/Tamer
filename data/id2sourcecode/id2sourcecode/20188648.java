    public static void silence() {
        if (isReady()) {
            for (int c = 0; c < getChannels().length; c++) getChannel(c).allNotesOff();
        }
    }
