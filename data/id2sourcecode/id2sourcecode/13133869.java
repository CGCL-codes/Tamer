    public PartEvent createEvent(IRCEvent event) {
        return new PartEvent(event.getRawEventData(), event.getSession(), event.getNick(), event.getSession().getChannel(event.arg(0)), event.args().size() == 2 ? event.arg(1) : "");
    }
