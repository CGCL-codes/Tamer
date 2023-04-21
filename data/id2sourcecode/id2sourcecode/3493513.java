    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String args[]) {
        if (args.length < 2) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage: '!pm <Player> <Message>'");
            return;
        }
        String playerName = args[0];
        Player playerEx = Main.getInstance().getServer().getPlayer(playerName);
        if (playerEx == null) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error: Invalid player.");
            return;
        }
        String message;
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            messageBuilder.append(" ");
        }
        message = messageBuilder.toString().substring(0, messageBuilder.toString().length() - 1);
        playerEx.sendMessage(ChatColor.GOLD + "PM from " + sender + " (IRC): " + message);
        Main.getInstance().getIRCHandler().sendNotice(sender.getNick(), Colors.BROWN + "PM to " + Colors.OLIVE + playerEx.getDisplayName() + Colors.BROWN + ": " + message);
        Main.getInstance().getIRCHandler().sendMessage("@" + Main.getInstance().getConfigHandler().ircChannel, Colors.OLIVE + "PM from " + Colors.BROWN + sender.getNick() + " (IRC)" + Colors.OLIVE + " to " + Colors.BROWN + Utilities.fixName(playerEx) + Colors.OLIVE + ": " + Colors.NORMAL + message);
    }
