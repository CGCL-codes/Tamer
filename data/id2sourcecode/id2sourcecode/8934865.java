    public void publishMessage(String sessionId, Object body) {
        TideChannel channel = getChannel(sessionId);
        if (channel != null) {
            Message message = new AsyncMessage();
            message.setHeader(AsyncMessage.SUBTOPIC_HEADER, "tide.events." + sessionId);
            message.setClientId(channel.getClientId());
            message.setDestination(DESTINATION_NAME);
            message.setBody(body);
            getGravity().publishMessage(channel, message);
        }
    }
