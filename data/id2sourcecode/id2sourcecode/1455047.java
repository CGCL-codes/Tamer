    public void sendEmail(EmailMessage email) throws XpressoException {
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(mailConfig, auth);
        MimeMessage message = new MimeMessage(session);
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(mailConfig.getProperty("mail.host"), mailConfig.getProperty("mail.user"), mailConfig.getProperty("mail.pass"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()[0]));
            message.setFrom(new InternetAddress(email.getFrom()));
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            transport.send(message);
        } catch (MessagingException ex) {
            throw new XpressoException("Error sending email." + ex, ex);
        }
    }
