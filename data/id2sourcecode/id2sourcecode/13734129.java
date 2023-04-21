    public boolean login(String loginJID, String password) {
        boolean result = true;
        ConnectionConfiguration config = new ConnectionConfiguration("jabber.ru", 5222);
        try {
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login("kreab0t", password);
        } catch (XMPPException exc) {
            System.out.println("Connection Error: " + exc.toString());
            result = false;
        } catch (Exception exc) {
            System.out.println("fail: " + exc.toString());
        }
        return result;
    }
