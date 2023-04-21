    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String args[]) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (args.length < 1) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !admin [message]");
            return;
        }
        StringBuilder chatMessageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            chatMessageBuilder.append(args[i]);
            chatMessageBuilder.append(" ");
        }
        String chatMessage = chatMessageBuilder.toString().substring(0, chatMessageBuilder.toString().length());
        String output = ChatColor.GOLD + "Admin " + sender.getNick() + " (IRC): " + chatMessage;
        Utilities.sendAdminMessage(output);
        output = Colors.DARK_BLUE + "*** " + Colors.OLIVE + "Admin " + sender.getNick() + ":" + Colors.NORMAL + " " + chatMessage;
        Main.getInstance().getIRCHandler().sendMessage("@" + channel, output);
    }
