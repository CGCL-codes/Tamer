    public void send(EmailMessage messageData) throws EmailException {
        try {
            log.info("sending: " + messageData.toString());
            MimeMessage message = new MimeMessage(session);
            setFrom(message, messageData);
            setTo(message, messageData);
            message.setSubject(messageData.getSubject());
            message.setSentDate(new Date());
            if (messageData.getAttachments() == null) {
                log.debug("preparing text-only message");
                message.setText(messageData.getMessageText(), ENCODING);
            } else {
                log.debug("preparing multipart message");
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(messageData.getMessageText(), ENCODING);
                multipart.addBodyPart(textPart);
                AttachmentList attachments = messageData.getAttachments();
                Attachment attachment = null;
                MimeBodyPart attachmentPart = null;
                while (attachments.hasMoreAttachments()) {
                    attachment = attachments.next();
                    attachmentPart = new MimeBodyPart();
                    attachmentPart.setDataHandler(attachment.getDataHandler());
                    attachmentPart.setFileName(attachment.getFileName());
                    multipart.addBodyPart(attachmentPart);
                }
                message.setContent(multipart);
            }
            Transport.send(message);
            log.info("message sent");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EmailException(e.getMessage(), e);
        }
    }
