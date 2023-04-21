    public MessageRecipients send(MultiChannelMessage multiChannelMessage, MessageRecipients messageRecipients, MessageRecipient messageSender) throws RecipientException, MessageException {
        MultiChannelMessage mcmClone = getCached(multiChannelMessage);
        if (mcmClone == null) {
            mcmClone = (MultiChannelMessage) multiChannelMessage.clone();
        }
        if (mcmClone != null) {
            try {
                mcmClone.addAttachments(AttachmentUtilities.getAttachmentsForChannel(getChannelName(), MessageInternals.getAttachments(multiChannelMessage)));
                cache.put(multiChannelMessage, mcmClone);
            } catch (Exception e) {
                logger.error("multi-channel-message-channelise-attachments-failure", e);
            }
        } else {
            logger.error("multi-channel-message-clone-failure-send-failed-for", getChannelName());
            return messageRecipients;
        }
        MessageRecipients failures = new MessageRecipients();
        MessageRecipients deviceSpecificRecips = new MessageRecipients();
        String currentDeviceName = null;
        Iterator recipientsIterator = messageRecipients.getIterator();
        while (recipientsIterator.hasNext()) {
            MessageRecipient recipient = (MessageRecipient) recipientsIterator.next();
            if (recipient.resolveDeviceName(false) != MessageRecipient.OK) {
                logger.warn("device-resolution-failed-for", recipient);
                recipient.setFailureReason(messageLocalizer.format("device-resolution-failed-for", recipient));
                failures.addRecipient(recipient);
                continue;
            }
            if (recipient.getChannelName().equals(channelName)) {
                if (currentDeviceName != null) {
                    if (!currentDeviceName.equals(recipient.getDeviceName())) {
                        boolean failed = false;
                        Exception cause = null;
                        try {
                            MessageRecipients localFails = sendImpl(multiChannelMessage, deviceSpecificRecips, messageSender);
                            Iterator lfIterator = localFails.getIterator();
                            while (lfIterator.hasNext()) {
                                failures.addRecipient((MessageRecipient) lfIterator.next());
                            }
                        } catch (MessageException me) {
                            failed = true;
                            cause = me;
                        } catch (RecipientException re) {
                            failed = true;
                            cause = re;
                        } finally {
                            if (failed) {
                                populateFailures(deviceSpecificRecips, failures, cause);
                                continue;
                            }
                        }
                        deviceSpecificRecips = new MessageRecipients();
                        currentDeviceName = recipient.getDeviceName();
                        deviceSpecificRecips.addRecipient(recipient);
                    } else {
                        deviceSpecificRecips.addRecipient(recipient);
                    }
                } else {
                    currentDeviceName = recipient.getDeviceName();
                    deviceSpecificRecips = new MessageRecipients();
                    deviceSpecificRecips.addRecipient(recipient);
                }
            }
        }
        boolean failed = false;
        Exception cause = null;
        try {
            MessageRecipients localFails = sendImpl(multiChannelMessage, deviceSpecificRecips, messageSender);
            Iterator lfIterator = localFails.getIterator();
            while (lfIterator.hasNext()) {
                failures.addRecipient((MessageRecipient) lfIterator.next());
            }
        } catch (MessageException me) {
            failed = true;
            cause = me;
        } catch (RecipientException re) {
            failed = true;
            cause = re;
        } finally {
            if (failed) {
                populateFailures(deviceSpecificRecips, failures, cause);
            }
        }
        return failures;
    }
