    public HashMap send(Email msg, boolean simulate) throws Exception {
        Address from = msg.getBaseHeader().getFrom()[0];
        Address[] to = msg.getBaseHeader().getTo();
        Address[] cc = msg.getBaseHeader().getCc();
        Address[] bcc = msg.getBaseHeader().getBcc();
        Address[] replyTo = msg.getBaseHeader().getReplyTo();
        Boolean requestReceiptNotification = msg.getBaseHeader().getRequestReceiptNotification();
        short priority = msg.getBaseHeader().getPriority();
        short sensitivity = msg.getBaseHeader().getSensitivity();
        SMTPMessage mimeMsg = new SMTPMessage(session);
        String subject = msg.getBaseHeader().getSubject();
        mimeMsg.setFrom(from);
        if (to != null) {
            mimeMsg.setRecipients(Message.RecipientType.TO, to);
        }
        if (cc != null) {
            mimeMsg.setRecipients(Message.RecipientType.CC, cc);
        }
        if (bcc != null) {
            mimeMsg.setRecipients(Message.RecipientType.BCC, bcc);
        }
        if (replyTo != null) {
            mimeMsg.setReplyTo(replyTo);
        }
        mimeMsg.setSentDate(new Date());
        if (subject == null || subject.length() == 0) {
            subject = "No subject";
        }
        if (requestReceiptNotification != null) {
            mimeMsg.addHeader("Disposition-Notification-To", from.toString());
        }
        if (priority > 0) {
            mimeMsg.addHeader("X-Priority", String.valueOf(priority));
            mimeMsg.addHeader("X-MSMail-Priority", EmailPriority.toStringValue(priority));
        }
        if (sensitivity > 0) {
            mimeMsg.addHeader("Sensitivity", EmailSensitivity.toStringValue(sensitivity));
        }
        String charset = "UTF-8";
        mimeMsg.setSubject(MimeUtility.encodeText(subject, charset, null));
        ArrayList parts = msg.getParts();
        EmailPart bodyPart = (EmailPart) parts.get(0);
        boolean isTextBody = (bodyPart.isHTMLText()) ? false : true;
        if (parts.size() == 1 && isTextBody) {
            mimeMsg.setText((String) bodyPart.getContent(), charset);
        } else {
            BodyPart bp = new MimeBodyPart();
            bp.setContent((String) bodyPart.getContent(), bodyPart.getContentType());
            bp.setHeader("Content-Type", bodyPart.getContentType());
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(bp);
            MimeBodyPart attPart = null;
            EmailPart myPart = null;
            DataSource ds = null;
            String tmpContType = null;
            int pos = -1;
            for (int i = 1; i < msg.getParts().size(); i++) {
                myPart = (EmailPart) msg.getParts().get(i);
                attPart = new MimeBodyPart();
                ds = myPart.getDataSource();
                if (ds == null) {
                    if (myPart.getContent() instanceof ByteArrayOutputStream) {
                        ByteArrayOutputStream bos = (ByteArrayOutputStream) myPart.getContent();
                        ds = new ByteArrayDataSource(bos.toByteArray(), myPart.getContentType(), myPart.getFileName());
                        attPart.setDataHandler(new DataHandler(ds));
                        bos.close();
                    } else if (myPart.getContent() instanceof ByteArrayInputStream) {
                        ByteArrayInputStream bis = (ByteArrayInputStream) myPart.getContent();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        int j = -1;
                        while ((j = bis.read()) != -1) {
                            bos.write(j);
                        }
                        ds = new ByteArrayDataSource(bos.toByteArray(), myPart.getContentType(), myPart.getFileName());
                        attPart.setDataHandler(new DataHandler(ds));
                        bos.close();
                        bis.close();
                    } else {
                        attPart.setContent(myPart.getContent(), myPart.getContentType());
                    }
                } else {
                    attPart.setDataHandler(new DataHandler(ds));
                }
                attPart.setDisposition(myPart.getDisposition());
                attPart.setFileName(MimeUtility.encodeText(myPart.getFilename(), charset, null));
                tmpContType = (myPart.getContentType() == null) ? "application/octet-stream" : myPart.getContentType();
                pos = tmpContType.indexOf(";");
                if (pos >= 0) {
                    tmpContType = tmpContType.substring(0, pos);
                }
                attPart.setHeader("Content-Type", tmpContType);
                multipart.addBodyPart(attPart);
            }
            mimeMsg.setContent(multipart);
        }
        mimeMsg.addHeader("X-Mailer", "Claros inTouch (http://www.claros.org)");
        mimeMsg.saveChanges();
        HashMap out = new HashMap();
        out.put("msg", mimeMsg);
        if (!simulate) {
            try {
                mimeMsg.setSendPartial(true);
                mimeMsg.setSentDate(new Date());
                Transport.send(mimeMsg);
                Address[] sent = mimeMsg.getAllRecipients();
                out.put("sent", sent);
            } catch (SendFailedException se) {
                Address[] sent = se.getValidSentAddresses();
                Address[] invalid = se.getInvalidAddresses();
                Address[] fail = se.getValidUnsentAddresses();
                out.put("sent", sent);
                out.put("invalid", invalid);
                out.put("fail", fail);
            }
        }
        return out;
    }
