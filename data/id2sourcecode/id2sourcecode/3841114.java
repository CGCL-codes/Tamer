    public IRCEvent createEvent(IRCEvent event) {
        Session session = event.getSession();
        String toWho = "";
        String byWho = session.getConnectedHostName();
        Channel chan = null;
        if (!session.isChannelToken(event.arg(0))) {
            toWho = event.arg(0);
            if (toWho.equals("AUTH")) toWho = "";
        } else {
            chan = session.getChannel(event.arg(0));
        }
        if (event.prefix().length() > 0) {
            if (event.prefix().contains("!")) {
                byWho = event.getNick();
            } else {
                byWho = event.prefix();
            }
        }
        return new NoticeEvent(event.getRawEventData(), event.getSession(), event.arg(1), toWho, byWho, chan);
    }
