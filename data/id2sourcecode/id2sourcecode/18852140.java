    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] arguments) {
        Player player = (Player) cs;
        if (!player.hasPermission(PermissionNodes.PERMISSION_MUTE_COMMAND)) {
            return true;
        }
        if (arguments.length == 0) {
            player.sendMessage(ChatColor.RED + "* Usage: '/mute <Player> (<Time in minutes>)'");
            return true;
        }
        Player mutePlayer = Main.getInstance().getServer().getPlayer(arguments[0]);
        if (mutePlayer == null) {
            player.sendMessage(ChatColor.RED + "* Error: That player is not online.");
            return true;
        }
        int muteTime = -1;
        if (arguments.length > 1 && Utilities.isNumeric(arguments[1])) {
            muteTime = Integer.parseInt(arguments[1]);
        }
        if (muteTime == 0) {
            player.sendMessage(ChatColor.RED + "* Error: Invalid time value.");
            return true;
        }
        MinegroundPlayer playerInstance = Main.getInstance().getPlayer(mutePlayer);
        playerInstance.mutePlayer(muteTime);
        player.sendMessage(ChatColor.DARK_GREEN + mutePlayer.getName() + " has been muted " + ((muteTime > 0) ? ("for " + muteTime + " minutes.") : ("permanently.")));
        mutePlayer.sendMessage(ChatColor.RED + "You have been muted " + ((muteTime > 0) ? ("for " + muteTime + " minutes.") : ("permanently.")));
        StringBuilder ircMessageBuilder = new StringBuilder();
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append("* ");
        ircMessageBuilder.append(Utilities.fixName(player));
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append(" muted ");
        ircMessageBuilder.append(Utilities.fixName(mutePlayer));
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append(((muteTime > 0) ? (" for " + muteTime + " minutes.") : (" permanently.")));
        Main.getInstance().getIRCHandler().sendMessage("@" + Main.getInstance().getConfigHandler().ircChannel, ircMessageBuilder.toString());
        return true;
    }
