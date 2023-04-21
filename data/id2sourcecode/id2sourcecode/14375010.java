    public String resolveChannelName(MessageRecipient recipient) {
        try {
            if (null != recipient.getChannelName()) {
                return recipient.getChannelName();
            }
        } catch (RecipientException e) {
        }
        String deviceName = resolveDeviceName(recipient);
        if (deviceName != null) {
            String messageProtocol = ResolverUtilities.getDeviceMessageProtocol(deviceName);
            if ("MHTML".equals(messageProtocol)) {
                return SMTP_CHANNEL;
            } else if ("MMS".equals(messageProtocol)) {
                return MMSC_CHANNEL;
            } else if ("SMS".equals(messageProtocol)) {
                return SMSC_CHANNEL;
            }
        }
        return null;
    }
