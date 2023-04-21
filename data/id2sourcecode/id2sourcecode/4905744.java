    @Override
    public void connect() {
        if (conn == null || !isConnected()) {
            try {
                conn.connect();
                conn.login(credentials.getNick(), credentials.getPassword(), resource);
                conn.addPacketListener(new PrivateMessageListener(this, messageProcessor), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
