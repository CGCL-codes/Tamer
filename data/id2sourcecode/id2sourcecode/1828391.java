    public void changeInstrument(TGTrack track, int instrument, boolean percussion) {
        track.getChannel().setInstrument((short) instrument);
        if (percussion) {
            TGChannel.setPercussionChannel(track.getChannel());
            track.setStrings(TGSongManager.createPercussionStrings(getSongManager().getFactory(), track.getStrings().size()));
        } else {
            if (track.getChannel().isPercussionChannel()) {
                TGChannel tempChannel = this.songManager.getFreeChannel((short) instrument, false);
                track.getChannel().setChannel(tempChannel.getChannel());
                track.getChannel().setEffectChannel(tempChannel.getEffectChannel());
            }
        }
        this.songManager.updateChannel(track.getChannel());
    }
