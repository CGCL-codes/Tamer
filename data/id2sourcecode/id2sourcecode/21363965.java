    private XMPPConnection xmppInitialize() {
        int port = 5222;
        try {
            port = Integer.parseInt(this.port);
        } catch (NumberFormatException e) {
            System.err.println("WARNING: Invalid port number. Assuming port " + String.valueOf(port) + " instead.");
        }
        System.err.println("INFO: Connecting to " + this.host + ", port: " + String.valueOf(port) + ", service: " + this.service + ".");
        ConnectionConfiguration configuration = new ConnectionConfiguration(this.host, port, this.service);
        XMPPConnection connection = new XMPPConnection(configuration);
        try {
            connection.connect();
        } catch (XMPPException e) {
            System.err.println("ERROR: Failed to connect to server.");
            return null;
        }
        try {
            System.err.println("INFO: Trying to login as " + this.user + ".");
            connection.login(this.user, this.password);
            System.err.println("INFO: Logged in sucessfully.");
            return connection;
        } catch (XMPPException e) {
            System.err.println("ERROR: Failed to login to server.");
            return null;
        }
    }
