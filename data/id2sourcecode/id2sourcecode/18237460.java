    public void joinComplete(IRCEvent e) {
        JoinCompleteEvent jce = (JoinCompleteEvent) e;
        if (e.getSession().getChannel(jce.getChannel().getName()) == null) {
            e.getSession().addChannel(jce.getChannel());
            jce.getSession().sayRaw("MODE " + jce.getChannel().getName());
        }
    }
