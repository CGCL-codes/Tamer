    private MessageRecipients sendToChannelAdapters(MultiChannelMessage message, MessageRecipients recipients, MessageRecipient replyTo) throws MessageException {
        MessageRecipients failures = new MessageRecipients();
        Iterator recipientsIterator = recipients.getIterator();
        String currentChannel = null;
        while (recipientsIterator.hasNext()) {
            MessageRecipient recipient = (MessageRecipient) recipientsIterator.next();
            try {
                if (recipient.getChannelName() != null) {
                    if (!recipient.getChannelName().equals(currentChannel)) {
                        currentChannel = recipient.getChannelName();
                        MessageChannel channel = (MessageChannel) CHANNEL_MAP.get(currentChannel);
                        if (channel != null) {
                            try {
                                MessageRecipients sendFailures = channel.send(message, recipients, replyTo);
                                if (sendFailures != null) {
                                    RecipientInternals.addRecipients(failures, sendFailures);
                                }
                            } catch (RecipientException e) {
                                throw new MessageException(e);
                            }
                        } else {
                            logger.error("unconfigured-message-channel", currentChannel);
                            Iterator cfIterator = recipients.getIterator();
                            MessageRecipients channelFailures = new MessageRecipients();
                            String failMsg = messageLocalizer.format("unconfigured-message-channel", currentChannel);
                            while (cfIterator.hasNext()) {
                                MessageRecipient mr = (MessageRecipient) cfIterator.next();
                                if (mr.getChannelName().equals(currentChannel)) {
                                    mr.setFailureReason(failMsg);
                                    channelFailures.addRecipient(mr);
                                }
                            }
                            RecipientInternals.addRecipients(failures, channelFailures);
                        }
                    }
                }
            } catch (RecipientException rce) {
                throw new MessageException(rce);
            }
        }
        return failures;
    }
