    protected void tearDown() throws Exception {
        super.tearDown();
        for (int i = 0; i < getMaxConnections(); i++) {
            try {
                if (!getConnection(i).isConnected()) {
                    XMPPConnection con = getConnection(i);
                    con.connect();
                    con.login(getUsername(i), getUsername(i));
                } else if (!getConnection(i).isAuthenticated()) {
                    getConnection(i).login(getUsername(i), getUsername(i));
                }
                getConnection(i).getAccountManager().deleteAccount();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getConnection(i).isConnected()) {
                getConnection(i).disconnect();
            }
        }
    }
