    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws CommandException {
        Player player = (Player) sender;
        if (Main.getInstance().getPlayer(player).getReplyPlayer() == null) {
            player.sendMessage(ChatColor.RED + "* Error: You haven't received any PM recently.");
            return true;
        }
        Player playerEx = Main.getInstance().getPlayer(player).getReplyPlayer();
        if (Main.getInstance().getServer().getPlayer(playerEx.getName()) == null) {
            player.sendMessage(ChatColor.RED + "* Error: That player is not online.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "* Usage: '/r <Message>'");
            return true;
        }
        String chatMessage;
        StringBuilder chatMessageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            chatMessageBuilder.append(args[i]);
            chatMessageBuilder.append(" ");
        }
        chatMessage = chatMessageBuilder.toString().substring(0, chatMessageBuilder.toString().length() - 1);
        playerEx.sendMessage(ChatColor.GOLD + "PM from " + player.getDisplayName() + ": " + chatMessage);
        playerEx.sendMessage(ChatColor.GRAY + "Use '/r' to quickly reply to that message.");
        player.sendMessage(ChatColor.GOLD + "PM to " + playerEx.getDisplayName() + ": " + chatMessage);
        StringBuilder builder = new StringBuilder();
        builder.append(Colors.OLIVE);
        builder.append("PM from ");
        builder.append(Colors.BROWN);
        builder.append(Utilities.fixName(player));
        builder.append(Colors.OLIVE);
        builder.append(" to ");
        builder.append(Colors.BROWN);
        builder.append(Utilities.fixName(playerEx));
        builder.append(Colors.OLIVE);
        builder.append(": ");
        builder.append(Colors.NORMAL);
        builder.append(chatMessage);
        Main.getInstance().getIRCHandler().sendMessage("@" + Main.getInstance().getConfigHandler().ircChannel, builder.toString());
        Main.getInstance().getPlayer(playerEx).setReplyPlayer(player);
        return true;
    }
