    private void writeChannel(TGTrack track) {
        int header = 0;
        header = (track.isSolo()) ? header |= CHANNEL_SOLO : header;
        header = (track.isMute()) ? header |= CHANNEL_MUTE : header;
        writeHeader(header);
        TGChannel channel = getChannel(track.getSong(), track);
        GMChannelRoute gmChannelRoute = getChannelRoute(channel.getChannelId());
        writeByte(gmChannelRoute.getChannel1());
        writeByte(gmChannelRoute.getChannel2());
        writeByte(channel.getProgram());
        writeByte(channel.getVolume());
        writeByte(channel.getBalance());
        writeByte(channel.getChorus());
        writeByte(channel.getReverb());
        writeByte(channel.getPhaser());
        writeByte(channel.getTremolo());
    }
