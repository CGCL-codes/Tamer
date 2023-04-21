    public static void main(String[] args) {
        SmtpSsl smtp = null;
        String username = "n.nelda@gmail.com";
        String password = "XXXXXX";
        String to = "kalvis.apsitis@accenture.com";
        try {
            smtp = new SmtpSsl("smtp.gmail.com", 465);
            smtp.connect();
            smtp.login(username, password);
            EmailMessage message = new EmailMessage();
            message.setTo(to);
            message.setFrom(username);
            message.setSubject("Sending email via Gmail SMTP");
            message.setBody("This is the body of the message");
            smtp.send(message);
            smtp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
