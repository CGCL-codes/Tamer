    @Override
    public boolean isAcceptingChatEvent(ChatEvent inboundEvent) {
        return inboundEvent.getType() == ChatType.CHANNEL_TELL && StringUtils.equals(inboundEvent.getChannel(), channel) || inboundEvent.getType() == ChatType.OUTBOUND && inboundEvent.getMessage().startsWith(connector.getChannelTabPrefix(channel));
    }
