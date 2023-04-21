    public static void main(String[] args) throws XMPPException {
        XMPPConnection connection = new XMPPConnection("jabber.org");
        connection.connect();
        connection.login("panoramix9", "potion");
        Roster roster = connection.getRoster();
        int NbContacts = roster.getEntryCount();
        System.out.println(NbContacts);
        for (RosterEntry entry : roster.getEntries()) {
            System.out.println(entry);
        }
        Presence presence = roster.getPresence("panoramix9@jabber.org");
        System.out.println(presence);
        connection.disconnect();
    }
