    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String args[]) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (args.length < 2) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !kick [exact player name] [reason]");
            return;
        }
        String playerName = args[0];
        String kickReason;
        StringBuilder kickReasonBuilder = new StringBuilder();
        for (int index = 1; index < args.length; index++) {
            kickReasonBuilder.append(args[index]);
            kickReasonBuilder.append(" ");
        }
        kickReason = kickReasonBuilder.toString().substring(0, kickReasonBuilder.toString().length() - 1);
        if (!NameBanHandler.kickPlayer(playerName, sender.getNick(), kickReason)) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error:" + Colors.NORMAL + " That player is not online.");
            return;
        }
        StringBuilder kickMessageBuilder = new StringBuilder();
        kickMessageBuilder.append(Colors.TEAL);
        kickMessageBuilder.append("You've kicked ");
        kickMessageBuilder.append(Colors.DARK_GREEN);
        kickMessageBuilder.append(playerName);
        kickMessageBuilder.append(Colors.TEAL);
        kickMessageBuilder.append(" for ");
        kickMessageBuilder.append(Colors.DARK_GREEN);
        kickMessageBuilder.append(kickReason);
        kickMessageBuilder.append(Colors.TEAL);
        kickMessageBuilder.append(".");
        Main.getInstance().getIRCHandler().sendMessage(channel, kickMessageBuilder.toString());
    }
