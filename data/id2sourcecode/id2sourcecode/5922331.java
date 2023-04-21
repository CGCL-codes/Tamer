    public static boolean login(final LoginProgress progress, final String username, final String password, final String host, final String portStr, final String domain) {
        int port = XMPPPORT;
        String localDomain = domain;
        String localHost = host;
        canceled = false;
        if (localDomain == null) {
            localDomain = username.substring(username.indexOf("@") + 1);
        }
        if (portStr != null) {
            port = Integer.parseInt(portStr);
        }
        if (localHost == null) {
            if (username.endsWith("gmail.com")) {
                localHost = "talk.google.com";
            } else if (username.endsWith("xiaonei.com")) {
                localHost = "talk.xiaonei.com";
            } else {
                localHost = "talk.jabber.org";
            }
            System.out.println("Host:" + localHost);
        }
        ConnectionConfiguration config = new ConnectionConfiguration(localHost, port, localDomain);
        connection = new XMPPConnection(config);
        try {
            connection.connect();
        } catch (XMPPException e) {
            if (canceled) {
                return false;
            }
            progress.append("Failed");
            return false;
        }
        try {
            if (canceled) {
                connection.disconnect();
                return false;
            }
            progress.append("Connected.\n\tLogin...");
            connection.login(username, password);
        } catch (Exception e) {
            if (canceled) {
                connection.disconnect();
                return false;
            }
            progress.append("\n\tAuthen failed");
            return false;
        }
        if (canceled) {
            connection.disconnect();
            return false;
        }
        progress.append("Logined.\n\tInitial...");
        return true;
    }
