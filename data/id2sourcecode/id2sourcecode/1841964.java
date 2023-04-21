    public final CharSequence getSubscriberScript() {
        return "dojox.cometd.subscribe('/" + getChannelId() + "', " + getPartialSubscriber() + ");\n";
    }
