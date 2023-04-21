    public void subscriptionSelected(ChannelSubscriptionIF channelSubscription) {
        if (channelSubscription == null) {
            this.jtextArea.setText("");
            return;
        }
        printChannel(channelSubscription.getChannel());
        if (channelIF instanceof Channel) {
            Channel c = (Channel) channelIF;
            c.removeObserver(this);
            c.addObserver(this);
        }
    }
