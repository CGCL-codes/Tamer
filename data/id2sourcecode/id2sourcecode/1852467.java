    public boolean equals(Object obj) {
        if (obj instanceof ChannelEntry) {
            ChannelEntry channelEntry = (ChannelEntry) obj;
            return channelEntry.getChannelProgram() == this;
        }
        return this == obj;
    }
