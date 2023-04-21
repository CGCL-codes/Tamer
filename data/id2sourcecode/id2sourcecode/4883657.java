    public void subscriptionSelected(ChannelSubscriptionIF channelSubscription) {
        this.channelSubscription = channelSubscription;
        if (channelSubscription == null) {
            this.setEnable(this, false);
            return;
        }
        ChannelIF channel = channelSubscription.getChannel();
        if (channel instanceof Channel) {
            this.setEnable(this, true);
        } else {
            this.setEnable(this, false);
        }
    }
