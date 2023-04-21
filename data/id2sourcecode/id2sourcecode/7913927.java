    public Pop3 popBeforeSmtp() throws MailException {
        Pop3 pop = new Pop3(popHost, popUser, popPwd);
        getLogger().debug("Try to connect to host " + popHost + " for user " + popUser + "....");
        PopStatus status = pop.connect();
        if (status.OK()) getLogger().debug("Connection established..."); else throw new MailException("Connection to pop server failed...");
        if (status.OK()) status = pop.login();
        if (status.OK()) getLogger().debug("Login accepted!");
        if (status.OK()) pop.get_TotalMsgs();
        if (!status.OK()) throw new MailException("pop failed...");
        return pop;
    }
