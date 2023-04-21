    private boolean hasSameInfo(TGTrack track, PTTrackInfo info) {
        TGChannel tgChannel = this.manager.getChannel(track.getChannelId());
        if (tgChannel == null) {
            return false;
        }
        if (!info.getName().equals(track.getName())) {
            return false;
        }
        if (info.getInstrument() != tgChannel.getProgram()) {
            return false;
        }
        if (info.getVolume() != tgChannel.getVolume()) {
            return false;
        }
        if (info.getBalance() != tgChannel.getBalance()) {
            return false;
        }
        if (info.getStrings().length != track.stringCount()) {
            return false;
        }
        for (int i = 0; i < info.getStrings().length; i++) {
            if (info.getStrings()[i] != track.getString((i + 1)).getValue()) {
                return false;
            }
        }
        return true;
    }
