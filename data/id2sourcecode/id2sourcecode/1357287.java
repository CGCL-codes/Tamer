    public void connect() {
        new DMessage("Connection to " + Options.SERVER + " as " + user.getUsername() + "...");
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
            connection.login(user.getUsername(), user.getPassword());
            System.out.println("Logged in as " + user.getUsername() + ".");
            updatePresence(new Presence(Presence.Type.available));
            contactList.getList();
            contactList.defineGroups();
            startPacketListening();
        } catch (XMPPException ex) {
            new Error(this, 1, "Wrong Username or Password");
        }
        vCard = new MyVCard(this);
    }
