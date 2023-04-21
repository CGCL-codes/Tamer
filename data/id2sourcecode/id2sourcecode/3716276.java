    @Property(label = "label.channel", detail = "label.channel.detail.recording", group = "label.source")
    public String getChannelName() {
        ChannelInformation channel = this.getChannel();
        return channel == null ? "" : channel.getName();
    }
