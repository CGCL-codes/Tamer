    public int getChannelId(String channel) {
        for (int i = 0; i < TradeBot.availableChannels.length; i++) {
            if (channel.equalsIgnoreCase(TradeBot.availableChannels[i])) {
                return (int) Math.pow(2, i);
            }
        }
        return -1;
    }
