    public void join(IRCEvent e) {
        JoinEvent je = (JoinEvent) e;
        je.getChannel().addNick(je.getNick());
    }
