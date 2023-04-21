    private MessageRecipientsStatusContainer validateRecipients(MessageRecipients recipients, int recipientType) throws MessageException {
        MessageRecipientsStatusContainer container = new MessageRecipientsStatusContainer();
        MessageRecipients failures = new MessageRecipients();
        MessageRecipients successes = new MessageRecipients();
        container.setFailures(failures);
        container.setSuccesses(successes);
        MessageRecipient recipient;
        MessageRecipient clone = null;
        Iterator recipientsIterator = recipients.getIterator();
        while (recipientsIterator.hasNext()) {
            recipient = (MessageRecipient) recipientsIterator.next();
            try {
                clone = (MessageRecipient) recipient.clone();
                RecipientInternals.setRecipientType(clone, recipientType);
                int channelStatus = clone.resolveChannelName(false);
                int deviceStatus = clone.resolveDeviceName(false);
                if (clone.getChannelName() != null && clone.getDeviceName() != null) {
                    if (clone.getAddress() != null || clone.getMSISDN() != null) {
                        successes.addRecipient(clone);
                    } else {
                        clone.setFailureReason(messageLocalizer.format("address-resolution-failed-for", clone));
                        failures.addRecipient(clone);
                    }
                } else {
                    if (deviceStatus != MessageRecipient.OK) {
                        clone.setFailureReason(messageLocalizer.format("device-resolution-failed-for", clone));
                    } else if (channelStatus != MessageRecipient.OK) {
                        clone.setFailureReason(messageLocalizer.format("channel-resolution-failed-for", clone));
                    }
                    failures.addRecipient(clone);
                }
            } catch (CloneNotSupportedException e) {
                logger.error("unexpected-clonenotsupportedexception", e);
            } catch (RecipientException rce) {
                try {
                    clone.setFailureReason(rce.getMessage());
                    failures.addRecipient(clone);
                } catch (RecipientException rce2) {
                    final String messageKey = "recipient-exception-caught";
                    logger.error(messageKey);
                    throw new MessageException(localizer.format(messageKey), rce2);
                }
            }
        }
        return container;
    }
