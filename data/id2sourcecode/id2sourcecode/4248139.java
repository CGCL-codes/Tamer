    public void sendEmail(String subject, String HtmlMessage, String to) {
        Transport transport = null;
        try {
            Properties props = prepareProperties();
            Session mailSession = Session.getInstance(props, new SMTPAuthenticator(from, password, true));
            transport = mailSession.getTransport("smtp");
            MimeMessage message = prepareMessage(mailSession, "ISO-8859-2", from, subject, HtmlMessage, to);
            System.out.println("PREPARE MESSSAGE OK");
            transport.connect();
            System.out.println("CONNECT OK");
            Transport.send(message);
            System.out.println("SEND OK");
        } catch (Exception ex) {
            System.out.println("exception == " + ex.getMessage());
        } finally {
            try {
                transport.close();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
