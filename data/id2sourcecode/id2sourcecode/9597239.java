    private String getInstrument(TGTrack track) {
        TGChannel channel = TuxGuitar.instance().getSongManager().getChannel(track.getChannelId());
        if (channel != null) {
            return (channel.getName() != null ? channel.getName() : new String());
        }
        return new String();
    }
