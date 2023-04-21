    protected void sendMessage(String messageName) throws Exception {
        EmailMessageBuilder messageBuilder = new EmailMessageBuilder(messageName);
        EmailMethodHelper emailMethodHelper = new TestEmailMethodHelper();
        EmailMethod emailMethod = new EmailMethod(getDfSession(), messageName, messageBuilder, emailMethodHelper, sendEmail());
        emailMethod.exec();
    }
