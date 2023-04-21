    @SuppressWarnings({ "JavaDoc" })
    public void sendMail() throws MessagingException {
        if (mailToAddress == null) throw new MessagingException("请你必须你填写收件人地址！");
        mailMessage.setContent(multipart);
        Transport.send(mailMessage);
    }
