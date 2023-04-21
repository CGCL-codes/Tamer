    public Jabber(UI program, String myName, String myPass, List<User> users) throws XMPPException {
        User me, u;
        this.program = program;
        me = new User(myName.substring(0, myName.indexOf('@')), myName, 0, "");
        me.myself = true;
        users.add(me);
        u = new User("*", "*@jabber", 0, "");
        u.realUser = false;
        users.add(u);
        this.users = users;
        try {
            connection = new XMPPConnection(new ConnectionConfiguration(myName.substring(myName.indexOf('@') + 1), 5222));
            connection.connect();
            connection.login(myName.substring(0, myName.indexOf('@')), myPass);
        } catch (XMPPException e) {
            DEBUG.error("Jabber: Jabber: cannot connect or authenticate to jabber server (wrong pass?");
            throw e;
        }
        chats = new ChatListener(this, connection.getChatManager());
        roster = connection.getRoster();
        if (roster != null) {
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            Collection<RosterEntry> e = roster.getEntries();
            for (RosterEntry r : e) {
                String user = r.getUser();
                String name = r.getName();
                if (name == null) name = user.substring(0, user.indexOf('@'));
                Presence p = roster.getPresence(user);
                if (User.find(users, user) == null) {
                    String status = p.getStatus();
                    if (status == null) status = new String();
                    Presence.Mode mode = p.getMode();
                    int avail;
                    if (mode != null) {
                        switch(mode) {
                            case available:
                            case chat:
                                avail = 3;
                                break;
                            case away:
                            case dnd:
                            case xa:
                                avail = 2;
                                break;
                            default:
                                avail = 0;
                        }
                    } else avail = 0;
                    u = new User(name, user, avail, status);
                    synchronized (users) {
                        users.add(u);
                    }
                } else {
                    DEBUG.warn("Jabber: userJoined: user " + user + " already exists");
                }
            }
            roster.addRosterListener(new RostListener(this));
            program.updateUsers(this);
        } else {
            DEBUG.error("Jabber: Jabber: No roster could be bound");
        }
        DEBUG.println("Jabber: Jabber: initialized");
    }
