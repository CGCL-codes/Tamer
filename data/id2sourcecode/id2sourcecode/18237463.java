    public void part(IRCEvent e) {
        PartEvent pe = (PartEvent) e;
        if (!pe.getChannel().removeNick(pe.getNick())) {
            log.severe("Could Not remove nick " + pe.getNick() + " from " + pe.getChannelName());
        }
        if (pe.getNick().equalsIgnoreCase(e.getSession().getNick())) {
            pe.getSession().removeChannel(pe.getChannel());
        }
    }
