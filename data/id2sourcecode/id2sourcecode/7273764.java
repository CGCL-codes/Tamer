    public String generateTargetMessageAsString(String deviceName) throws MessageException {
        if (deviceName != null) {
            ProtocolIndependentMessage rawMessage = (ProtocolIndependentMessage) rawMessageCache.get(deviceName);
            if (rawMessage == null) {
                MessageRequestor messageRequestor = MessageRequestor.getInstance();
                rawMessage = messageRequestor.getChannelIndependentMessage(deviceName, this);
                rawMessageCache.put(deviceName, rawMessage);
            }
            MessageAssembler messageAssembler = new PlainTextMessageAssembler();
            return (String) messageAssembler.assembleMessage(rawMessage, messageAttachments);
        } else {
            throw new MessageException(LOCALIZER.format("device-name-null-invalid"));
        }
    }
