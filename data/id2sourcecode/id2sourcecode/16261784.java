    public static void processLoginRequest(MessagesConsumer server, ClientThread thread, AbstractMessage message) throws Exception {
        LoginRequest loginRequest = (LoginRequest) message;
        String salt = (String) thread.getProperty("login.salt");
        String toDigest = salt + "test";
        server.getLogger().info("To digest: '" + toDigest + "'");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte bt[] = digest.digest(toDigest.getBytes());
        String localPassword = byteArrayToHexString(bt).toLowerCase();
        boolean ok = localPassword.equalsIgnoreCase(loginRequest.getPassword());
        server.getLogger().info("Digest: '" + localPassword + "'");
        server.getLogger().info("Received: '" + loginRequest.getPassword() + "'");
        server.getLogger().info("Result = '" + ok + "'");
        LoginResponse response = new LoginResponse();
        if (ok) {
            thread.setSessionId((int) (Math.random() * 10000000));
            thread.setLogin(loginRequest.getLogin());
            response.setScene(KADATHConfig.getProperty("com.ngranek.unsolved.test.level"));
        } else {
            thread.setSessionId(-1);
        }
        response.setSessionId(thread.getSessionId());
        thread.sendMessage(response);
    }
