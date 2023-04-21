    public TaskExample() {
        ConnectionManager conman = new ConnectionManager(new Profile("scripy"));
        Session session = conman.requestConnection("irc.freenode.net");
        session.onEvent(new TaskImpl("join_channels") {

            public void receiveEvent(IRCEvent e) {
                e.getSession().join("#jerklib");
            }
        }, Type.CONNECT_COMPLETE);
        session.onEvent(new TaskImpl("hello") {

            public void receiveEvent(IRCEvent e) {
                JoinCompleteEvent jce = (JoinCompleteEvent) e;
                jce.getChannel().say("Hello from JerkLib!");
            }
        }, Type.JOIN_COMPLETE);
        session.onEvent(new TaskImpl("motd_join") {

            public void receiveEvent(IRCEvent e) {
                if (e.getType() == Type.MOTD) {
                    MotdEvent me = (MotdEvent) e;
                    System.out.println(me.getMotdLine());
                } else {
                    JoinCompleteEvent je = (JoinCompleteEvent) e;
                    je.getChannel().say("Yay tasks!");
                }
            }
        }, Type.MOTD, Type.JOIN_COMPLETE);
        session.onEvent(new TaskImpl("print") {

            public void receiveEvent(IRCEvent e) {
                System.out.println(e.getRawEventData());
            }
        });
    }
