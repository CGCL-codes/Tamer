    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws CommandException {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "* Usage: '/ircpm <Player> <Message>'");
            return true;
        }
        String playerName = args[0];
        User ircUser;
        ircUser = Main.getInstance().getIRCHandler().getUser(playerName, Main.getInstance().getConfigHandler().ircChannel);
        if (ircUser == null) {
            player.sendMessage(ChatColor.RED + "* Invalid user.");
            return true;
        }
        String chatMessage;
        StringBuilder chatMessageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            chatMessageBuilder.append(args[i]);
            chatMessageBuilder.append(" ");
        }
        chatMessage = chatMessageBuilder.toString().substring(0, chatMessageBuilder.toString().length() - 1);
        player.sendMessage(ChatColor.GOLD + "PM to " + ircUser.getNick() + " (IRC): " + chatMessage);
        Main.getInstance().getIRCHandler().sendMessage("&" + Main.getInstance().getConfigHandler().ircChannel, Colors.OLIVE + "PM from " + Colors.BROWN + Utilities.fixName(player) + Colors.OLIVE + " to " + Colors.BROWN + ircUser.getNick() + " (IRC)" + Colors.OLIVE + ": " + Colors.NORMAL + chatMessage);
        Main.getInstance().getIRCHandler().sendMessage(ircUser.getNick(), Colors.BROWN + "Ingame PM from " + Colors.OLIVE + Utilities.fixName(player) + Colors.NORMAL + Colors.BROWN + ": " + Colors.NORMAL + chatMessage);
        return true;
    }
