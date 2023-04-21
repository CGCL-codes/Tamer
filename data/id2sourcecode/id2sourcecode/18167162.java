    private void writeChannel(TGChannel channel) {
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
