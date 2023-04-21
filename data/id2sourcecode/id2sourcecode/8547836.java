    public void giveI3ChannelsList(MOB mob) {
        if ((mob == null) || (!i3online())) return;
        if (mob.isMonster()) return;
        StringBuffer buf = new StringBuffer("\n\rI3 Channels List:\n\r");
        ChannelList list = Intermud.getAllChannelList();
        if (list != null) {
            Hashtable l = list.getChannels();
            for (Enumeration e = l.elements(); e.hasMoreElements(); ) {
                Channel c = (Channel) e.nextElement();
                if (c.type == 0) buf.append("[" + CMStrings.padRight(c.channel, 20) + "] " + c.owner + "\n\r");
            }
        }
        mob.session().wraplessPrintln(buf.toString());
    }
