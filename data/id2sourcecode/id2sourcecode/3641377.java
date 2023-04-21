    public void sendMail() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAddress, password);
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderAddress));
        if (toAddressList != null && toAddressList.size() > 0) {
            for (KMailAddress address : toAddressList) {
                message.addRecipient(RecipientType.TO, address.getInternetAddress());
            }
        }
        if (ccAddressList != null && ccAddressList.size() > 0) {
            for (KMailAddress address : ccAddressList) {
                message.addRecipient(RecipientType.CC, address.getInternetAddress());
            }
        }
        if (bccAddressList != null && bccAddressList.size() > 0) {
            for (KMailAddress address : bccAddressList) {
                message.addRecipient(RecipientType.BCC, address.getInternetAddress());
            }
        }
        message.setSubject(subject);
        message.setSentDate(new Date());
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body, "text/html;charset=utf-8");
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
