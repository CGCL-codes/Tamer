    private void writePayload(MessageEvent messageEvent, ChannelBuffer extrasBuffer, ChannelBuffer keyBuffer, ChannelBuffer valueBuffer, ChannelBuffer headerBuffer) {
        if (messageEvent.getChannel().isOpen()) {
            messageEvent.getChannel().write(headerBuffer);
            if (extrasBuffer != null) messageEvent.getChannel().write(extrasBuffer);
            if (keyBuffer != null) messageEvent.getChannel().write(keyBuffer);
            if (valueBuffer != null) messageEvent.getChannel().write(valueBuffer);
        }
    }
