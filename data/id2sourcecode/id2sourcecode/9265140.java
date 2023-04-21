    public Recipient replyRecipient() {
        return new IRCChannelRecipient(generatedBy, getChannel());
    }
