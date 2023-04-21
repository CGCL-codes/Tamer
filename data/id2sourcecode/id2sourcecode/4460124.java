    public boolean connect() {
        connection = new XMPPConnection(host);
        try {
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
        try {
            connection.login(username, password, resource);
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
