    public void connect() {
        theChats = new ArrayList<Chat>();
        myConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        myCon = new XMPPConnection(myConfig);
        try {
            myCon.connect();
            myCon.login(theSettings.getUsername(), theSettings.getPassword(), "Home");
            while (!myCon.isAuthenticated()) {
            }
            myCon.getChatManager().addChatListener(this);
            myCon.getRoster().addRosterListener(this);
            ArrayList<RosterEntry> Entries = Collections.list(Collections.enumeration(myCon.getRoster().getEntries()));
            for (RosterEntry re : Entries) {
                Buddy myBuddy = new Buddy();
                myBuddy.setAccount(this);
                myBuddy.setScreename(re.getUser());
                myBuddy.setAlias(re.getName());
                myBuddy.setStatus(new Status(Status.offline));
                theEvents.buddyStatusChange(myBuddy, true);
            }
            theEvents.loggedIn(this);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
