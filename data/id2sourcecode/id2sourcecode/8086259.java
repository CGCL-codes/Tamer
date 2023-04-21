    public IRCEvent createEvent(IRCEvent event) {
        Session session = event.getSession();
        Channel channel = session.getChannel(event.arg(0));
        String msg = "";
        if (event.args().size() == 3) {
            msg = event.arg(2);
        }
        return new KickEvent(event.getRawEventData(), session, event.getNick(), event.arg(1), msg, channel);
    }
