    public void sendMail(Message message, MailAddress address) {
        getSessionHandler();
        if (session == null) {
            session = new SmtpSocketSession();
        }
        if (!smtpSessionHandler.isConnected()) {
            smtpSessionHandler.connect(session, this);
            if (!smtpSessionHandler.isConnected()) return;
            if (requiresLogin()) {
                if (getUserName() == null || getPassword() == null) return;
                session.login(getUserName(), getPassword());
                if (smtpSessionHandler.isLoggedIn()) {
                } else {
                    session.quit();
                    return;
                }
            }
        }
        message.setField(RFC822.FROM, address.toString());
        try {
            session.mailFrom(getMailAddress().getEmail());
        } catch (IOException f) {
        }
        AddressList list = message.getTo();
        MailAddress[] recipients = list.getAddresses();
        for (int i = 0; i < recipients.length; i++) {
            session.rcptTo(recipients[i].getEmail());
        }
        session.data(message);
        session.sendMessage();
        message.setFlag(Message.SENT);
        smtpSessionHandler.disconnect();
    }
