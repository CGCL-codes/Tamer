    public void kick(IRCEvent e) {
        KickEvent ke = (KickEvent) e;
        if (!ke.getChannel().removeNick(ke.getWho())) {
            log.info("COULD NOT REMOVE NICK " + ke.getWho() + " from channel " + ke.getChannel().getName());
        }
        Session session = e.getSession();
        if (ke.getWho().equals(session.getNick())) {
            session.removeChannel(ke.getChannel());
            if (session.isRejoinOnKick()) {
                session.join(ke.getChannel().getName());
            }
        }
    }
