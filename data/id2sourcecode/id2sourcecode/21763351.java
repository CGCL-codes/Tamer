    private MimeMessage initializeMimeMessageWithAddresses() throws XAwareConfigurationException, XAwareSubstitutionException, XAwareConfigMissingException, XAwareException, MessagingException {
        String authValue = m_bizDriver.getChannelSpecification().getProperty(SmtpChannelSpecification.AUTH_KEY);
        final Properties props = new Properties();
        props.setProperty(SmtpChannelSpecification.AUTH_KEY, authValue);
        final Session session = Session.getDefaultInstance(props, null);
        final MimeMessage message = new MimeMessage(session);
        Address[] toAddr = m_translator.getToAddr();
        message.addRecipients(Message.RecipientType.TO, toAddr);
        Address[] ccAddr = m_translator.getCcAddr();
        if (ccAddr.length != 0) {
            message.addRecipients(Message.RecipientType.CC, ccAddr);
        }
        Address[] bccAddr = m_translator.getBccAddr();
        if (bccAddr.length != 0) {
            message.addRecipients(Message.RecipientType.BCC, bccAddr);
        }
        Address[] fromAddr = m_translator.getFromAddr();
        message.addFrom(fromAddr);
        Address[] replyAddr = m_translator.getBccAddr();
        if (replyAddr.length != 0) {
            message.setReplyTo(replyAddr);
        }
        return message;
    }
