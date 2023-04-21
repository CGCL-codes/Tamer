    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id")
    public IRCChannelBean getChannel() {
        return channel;
    }
