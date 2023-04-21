    public void enviarEmail(Servidor servidor, Email email) throws EmailException {
        try {
            MimeMessage message = defineEmail(servidor, email);
            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            throw new EmailException(e);
        } catch (MessagingException e) {
            throw new EmailException(e);
        }
    }
