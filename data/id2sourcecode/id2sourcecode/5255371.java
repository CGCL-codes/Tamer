    private void login() throws XMPPException {
        if (anonymous) connection.loginAnonymously(); else {
            try {
                connection.login(user, password, thisPeer != null && thisPeer.getGraph() != null ? thisPeer.getIdentity().getId().toString() : null);
            } catch (XMPPException ex) {
                if (ex.getMessage().indexOf("authentication failed") > -1 && autoRegister && connection.getAccountManager().supportsAccountCreation()) {
                    connection.getAccountManager().createAccount(user, password);
                    connection.disconnect();
                    connection.connect();
                    connection.login(user, password);
                } else throw ex;
            }
        }
    }
