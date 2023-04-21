    @Property(group = "label.source", display = DisplayHint.ICON)
    public URI getSmallIcon() {
        ChannelInformation channel = this.getChannel();
        return channel == null ? null : channel.getSmallIcon();
    }
