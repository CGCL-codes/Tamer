    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] arguments) {
        Player player = (Player) cs;
        if (!player.hasPermission(PermissionNodes.PERMISSION_UNMUTE_COMMAND)) {
            return true;
        }
        if (arguments.length == 0) {
            player.sendMessage(ChatColor.RED + "* Usage: '/unmute <Player>'");
            return true;
        }
        Player mutePlayer = Main.getInstance().getServer().getPlayer(arguments[0]);
        if (mutePlayer == null) {
            player.sendMessage(ChatColor.RED + "* Error: That player is not online.");
            return true;
        }
        MinegroundPlayer playerInstance = Main.getInstance().getPlayer(mutePlayer);
        if (playerInstance.isMuted() == MinegroundPlayer.MUTED_NOTMUTED) {
            player.sendMessage(ChatColor.RED + "* Error: That player isn't muted.");
            return true;
        }
        playerInstance.unmutePlayer();
        player.sendMessage(ChatColor.DARK_GREEN + "* You have unmuted " + mutePlayer.getName() + ".");
        mutePlayer.sendMessage(ChatColor.DARK_GREEN + "* You have been unmuted.");
        StringBuilder ircMessageBuilder = new StringBuilder();
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append("* ");
        ircMessageBuilder.append(Utilities.fixName(player));
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append(" unmuted ");
        ircMessageBuilder.append(Utilities.fixName(mutePlayer));
        ircMessageBuilder.append(Colors.BROWN);
        ircMessageBuilder.append(".");
        Main.getInstance().getIRCHandler().sendMessage("@" + Main.getInstance().getConfigHandler().ircChannel, ircMessageBuilder.toString());
        return true;
    }
