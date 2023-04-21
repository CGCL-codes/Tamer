    @Override
    public void invoke(IChatter bot, String playerName, String[] params) {
        try {
            String channel = Parser.parseString(params, 0);
            int channelId = bot.getChannelId(channel);
            if (channelId <= 0) {
                bot.appendAnserw(playerName, Locale.MSG_AVAILABLE_CHANNELS);
                bot.sendEmote(Emote.ScratchHead);
            } else {
                ChatableUser user = bot.getChatableUser(playerName);
                if (user != null) {
                    user.leaveChannel(channelId);
                    bot.appendAnserw(playerName, String.format(Locale.MSG_LEFT_CHANNEL, channel));
                }
            }
        } catch (ParseException pe) {
            this.onSyntaxError(bot, playerName);
        }
    }
