    public static void sendMailMessage(final String from, final String to, String subject, final String encoding, final String contentType, final String txtMessage, final String server) throws MessagingException {
        Properties mailProps = new Properties();
        mailProps.put("mail.transport.protocol", "smtp");
        mailProps.put("mail.smtp.host", server == null || server.length() == 0 ? "localhost" : server);
        javax.mail.Session session = javax.mail.Session.getInstance(mailProps);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        setAddress(to, Message.RecipientType.TO, message);
        if (encoding == null) {
            message.setSubject(subject);
        } else {
            message.setSubject(subject, encoding);
        }
        MimeBodyPart mbp1 = new MimeBodyPart();
        if ("text/plain".equals(contentType)) {
            mbp1.setText(txtMessage, encoding);
        } else {
            mbp1.setContent(txtMessage, contentType);
        }
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        message.setContent(mp);
        message.setSentDate(new Date());
        Transport.send(message);
    }
