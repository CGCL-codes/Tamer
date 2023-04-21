    @Path("/{ChannelID}/")
    public final Channel getChannel(@PathParam("ChannelID") final String channelID) {
        if (channelID == null) return null;
        if (channels != null) {
            for (Channel channel : channels) {
                if ((channel == null) || (!channel.getDefinition().getID().equals(channelID))) continue;
                return channel;
            }
        }
        return null;
    }
