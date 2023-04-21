    public boolean isOnline(Bot bot, String nick) {
        Channel channel = bot.getState().getChannel(bot.getChannelName());
        for (Enumeration e = channel.getMembers(); e.hasMoreElements(); ) {
            Member member = (Member) e.nextElement();
            if (member.getNick().getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }
