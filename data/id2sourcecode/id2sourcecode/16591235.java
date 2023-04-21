    public void subscriptionSelected(ChannelSubscriptionIF channelSubscription) {
        String summary = "";
        summary += "Title: " + channelSubscription.toString() + '\n' + "Active: " + (channelSubscription.isActive() ? "yes" : "no") + '\n' + "Refresh Time: " + String.valueOf(channelSubscription.getUpdateInterval()) + " seconds" + '\n' + "Location: " + channelSubscription.getChannel().getLocation();
        jtextArea.setText(summary);
    }
