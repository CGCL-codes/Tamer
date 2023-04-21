    private int getClef(TGTrack track) {
        if (!isPercussionChannel(track.getSong(), track.getChannelId())) {
            Iterator it = track.getStrings().iterator();
            while (it.hasNext()) {
                TGString string = (TGString) it.next();
                if (string.getValue() <= 34) {
                    return TGMeasure.CLEF_BASS;
                }
            }
        }
        return TGMeasure.CLEF_TREBLE;
    }
