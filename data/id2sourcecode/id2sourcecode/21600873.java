    private void setTrackInfo(TGTrack tgTrack, PTTrackInfo info) {
        TGChannel tgChannel = this.manager.addChannel();
        tgChannel.setProgram((short) info.getInstrument());
        tgChannel.setVolume((short) info.getVolume());
        tgChannel.setBalance((short) info.getBalance());
        tgTrack.setName(info.getName());
        tgTrack.setChannelId(tgChannel.getChannelId());
        tgTrack.getStrings().clear();
        for (int i = 0; i < info.getStrings().length; i++) {
            TGString string = this.manager.getFactory().newString();
            string.setNumber((i + 1));
            string.setValue(info.getStrings()[i]);
            tgTrack.getStrings().add(string);
        }
    }
