    public static String getInstrumentForPart(jm.music.data.Part part) {
        if (part.getChannel() == DRUM_CHANNEL) {
            return spacelessDrumNameFromNumber(part.getInstrument());
        } else {
            return getInstrumentName(part.getInstrument());
        }
    }
