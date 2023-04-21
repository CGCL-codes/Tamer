    public User login(String username, String password) throws LoginException, ParsePacketException, IOException, ExceptionInInitializerError, DatabaseException {
        GMConnector connector = new GMConnector(username, password, 1);
        connector.connect();
        if (!connector.isConnected()) {
            throw new LoginException("Failed to connect");
        }
        String auth = UUID.randomUUID().toString();
        SessionServer sessionServer = new SessionServer();
        sessionServer.login(username, auth);
        String name = "";
        ArrayList contacts = new ArrayList();
        name = connector.getUser();
        GMContact[] gm_contacts = connector.getContact(0, "");
        for (int i = 0; i < gm_contacts.length; i++) {
            contacts.add(new Contact(gm_contacts[i].getName(), gm_contacts[i].getEmail()));
        }
        User user = new User(name, contacts, auth);
        UserCacheServer user_cache_server = new UserCacheServer();
        user_cache_server.cacheUser(auth, user);
        return user;
    }
