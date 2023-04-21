    private void checkTracks() {
        Iterator it = this.tracks.iterator();
        while (it.hasNext()) {
            TGTrack track = (TGTrack) it.next();
            TGChannel trackChannel = null;
            Iterator tcIt = this.tempChannels.iterator();
            while (tcIt.hasNext()) {
                TempChannel tempChannel = (TempChannel) tcIt.next();
                if (tempChannel.getTrack() == track.getNumber()) {
                    Iterator channelIt = this.channels.iterator();
                    while (channelIt.hasNext()) {
                        TGChannel tgChannel = (TGChannel) channelIt.next();
                        GMChannelRoute gmChannelRoute = this.channelRouter.getRoute(tgChannel.getChannelId());
                        if (gmChannelRoute != null && tempChannel.getChannel() == gmChannelRoute.getChannel1()) {
                            trackChannel = tgChannel;
                        }
                    }
                }
            }
            if (trackChannel != null) {
                track.setChannelId(trackChannel.getChannelId());
            }
            if (trackChannel != null && trackChannel.isPercussionChannel()) {
                track.setStrings(TGSongManager.createPercussionStrings(this.factory, 6));
            } else {
                track.setStrings(getTrackTuningHelper(track.getNumber()).getStrings());
            }
        }
    }
