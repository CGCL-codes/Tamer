    public void sendMail(MailObject mailObject) throws AddressException, MessagingException {
        MimeMessage msg = new MimeMessage(session);
        initSender(mailObject, msg);
        initTo(mailObject, msg);
        initCC(mailObject, msg);
        initCCN(mailObject, msg);
        initSubject(mailObject, msg);
        MimeBodyPart messageBodyPart = initAttachments(mailObject, msg);
        initBody(mailObject, messageBodyPart);
        Transport.send(msg);
    }
