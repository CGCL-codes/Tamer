    public JiahoConnection(final String host, final int port, final String username, final String password, final boolean newAccount) {
        try {
            this.username = username;
            this.password = password;
            this.host = host;
            this.config = new ConnectionConfiguration(host, port);
            this.connection = new XMPPConnection(this.config);
            this.connection.connect();
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            this.config.setSASLAuthenticationEnabled(true);
            if (newAccount) {
                this.connection.getAccountManager().createAccount(username, password);
                this.connection.login(username, password, serviceName);
                this.connection.addConnectionListener(this);
            } else {
                this.connection.login(username, password, serviceName);
                this.connection.addConnectionListener(this);
            }
            JiahoLog.Info(JiahoConnection.class, this.connection.getUser() + " : " + JiahoGeneralProperties.InfoLogin);
        } catch (final XMPPException e) {
            JiahoLog.Warn(JiahoConnection.class, this.username + "@" + this.host + " : " + JiahoGeneralProperties.WarnLogin, e);
        }
    }
