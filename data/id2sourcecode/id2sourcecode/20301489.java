    @Property(label = "label.channel", detail = "label.channel.detail.event", group = "label.source")
    public String getChannelName() {
        return this.locateString(PO.broadcast_on, TMSNet.label);
    }
