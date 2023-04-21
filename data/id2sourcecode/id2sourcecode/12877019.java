    public ProtocolIndependentMessage getChannelIndependentMessage(String deviceName, MultiChannelMessage message) throws MessageException {
        ProtocolIndependentMessage rawMessage = null;
        if (message.getMessage() != null) {
            rawMessage = doInternalRequest(deviceName, message.getMessage(), message.getCharacterEncoding());
        } else if (message.getMessageURL() != null) {
            rawMessage = doExternalRequest(deviceName, message.getMessageURL(), message.getCharacterEncoding());
            if (rawMessage != null) {
                rawMessage.setBaseURL(message.getMessageURL());
            }
        }
        return rawMessage;
    }
