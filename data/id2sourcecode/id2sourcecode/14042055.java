    @Property(group = "label.source", display = DisplayHint.ICON)
    public URI getLargeIcon() {
        ChannelInformation channel = this.getChannel();
        return channel == null ? null : channel.getLargeIcon();
    }
