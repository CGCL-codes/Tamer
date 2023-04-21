    public MimeMultipart generateTargetMessageAsMimeMultipart(String deviceName) throws MessageException {
        if (deviceName != null) {
            MimeMultipart mimeMultipart = (MimeMultipart) mimeMessageCache.get(deviceName);
            if (mimeMultipart == null) {
                ProtocolIndependentMessage rawMessage = (ProtocolIndependentMessage) rawMessageCache.get(deviceName);
                if (rawMessage == null) {
                    MessageRequestor messageRequestor = MessageRequestor.getInstance();
                    rawMessage = messageRequestor.getChannelIndependentMessage(deviceName, this);
                    rawMessageCache.put(deviceName, rawMessage);
                }
                MessageAssembler messageAssembler = new MimeMessageAssembler();
                MessageAttachments attachments = AttachmentUtilities.getAttachmentsForDevice(deviceName, messageAttachments);
                mimeMultipart = (MimeMultipart) messageAssembler.assembleMessage(rawMessage, attachments);
                mimeMessageCache.put(deviceName, mimeMultipart);
            }
            return mimeMultipart;
        } else {
            throw new MessageException(LOCALIZER.format("device-name-null-invalid"));
        }
    }
