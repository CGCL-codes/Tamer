    protected void sendMessage(String messageName) throws Exception {
        DaafEmailMessageBuilder messageBuilder = new DaafEmailMessageBuilder(messageName);
        EmailMethodHelper emailMethodHelper = new TestEmailMethodHelper();
        EmailMethod emailMethod = new EmailMethod(getDfSession(), messageName, messageBuilder, emailMethodHelper, sendEmail());
        emailMethod.runMethod();
    }
