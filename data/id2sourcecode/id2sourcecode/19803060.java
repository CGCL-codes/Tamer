    private void writePayload(final MessageEvent messageEvent, final ChannelBuffer extrasBuffer, final ChannelBuffer keyBuffer, final ChannelBuffer valueBuffer, final ChannelBuffer headerBuffer) {
        if (messageEvent.getChannel().isOpen()) {
            messageEvent.getChannel().write(headerBuffer);
            if (extrasBuffer != null) {
                messageEvent.getChannel().write(extrasBuffer);
            }
            if (keyBuffer != null) {
                messageEvent.getChannel().write(keyBuffer);
            }
            if (valueBuffer != null) {
                messageEvent.getChannel().write(valueBuffer);
            }
        }
    }
