    public boolean createAccount(PlayerData data) throws Exception {
        try {
            character = data.getCharacterName();
            clientManager.connect(data.getServer(), data.getPort(), true);
            if (clientManager.createAccount(data.getPlayerName(), data.getPassword(), data.getEmail()) == false) {
                String result = clientManager.getEvent();
                if (result == null) {
                    result = "The server is not responding. Please check that it is online, and that you " + "supplied the correct details.";
                }
                throw new Exception(result);
            }
        } catch (ariannexpTimeoutException ex) {
            String result = "Unable to connect to server to create your account. " + "The server may be down or, if you are using a custom server, you may have entered its " + "name and port number incorrectly.";
            throw new Exception(result);
        }
        try {
            if (clientManager.login(data.getPlayerName(), data.getPassword()) == false) {
                String result = clientManager.getEvent();
                if (result == null) {
                    result = "Unable to connect to server. The server may be down or, if you are using a " + "custom server, you may have entered its name and port number incorrectly.";
                    throw new Exception(result);
                }
            } else {
                int i = 0;
                while (!transferred) {
                    clientManager.loop(0);
                }
                transferred = false;
                setInitialAtributes(data.getRace(), data.getSex());
                while (i++ < 30) {
                    clientManager.loop(0);
                }
                while (clientManager.logout() == false) {
                    ;
                }
            }
        } catch (ariannexpTimeoutException ex) {
            String result = "Unable to connect to the server. The server may be down or, if you are using a " + "custom server, you may have entered its name and port number incorrectly.";
            throw new Exception(result);
        }
        return true;
    }
