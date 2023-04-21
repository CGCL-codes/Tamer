    public IRCEvent createEvent(IRCEvent event) {
        Session session = event.getSession();
        if (!event.getNick().equalsIgnoreCase(event.getSession().getNick())) {
            return new JoinEvent(event.getRawEventData(), session, session.getChannel(event.arg(0)));
        }
        return new JoinCompleteEvent(event.getRawEventData(), event.getSession(), new Channel(event.arg(0), event.getSession()));
    }
