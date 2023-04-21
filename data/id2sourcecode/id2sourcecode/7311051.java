    private Message handleUnsubscribeMessage(CommandMessage message) {
        Channel channel = getChannel((String) message.getClientId());
        if (channel == null) return handleUnknownClientMessage(message);
        AsyncMessage reply = null;
        ServiceAdapter adapter = adapterFactory.getServiceAdapter(message);
        reply = (AcknowledgeMessage) adapter.manage(channel, message);
        postManage(channel);
        reply.setDestination(message.getDestination());
        reply.setClientId(channel.getId());
        reply.getHeaders().putAll(message.getHeaders());
        return reply;
    }
