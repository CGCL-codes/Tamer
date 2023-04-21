    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String command, String[] arguments) {
        Player player = (Player) cs;
        if (!player.hasPermission(PermissionNodes.PERMISSION_KICK_COMMAND)) {
            return true;
        }
        if (arguments.length < 2) {
            player.sendMessage(ChatColor.RED + "* Usage: '/kick <exact player name> <reason>'");
            return true;
        }
        String playerName = arguments[0];
        String kickReason;
        StringBuilder kickReasonBuilder = new StringBuilder();
        for (int index = 1; index < arguments.length; index++) {
            kickReasonBuilder.append(arguments[index]);
            kickReasonBuilder.append(" ");
        }
        kickReason = kickReasonBuilder.toString().substring(0, kickReasonBuilder.toString().length() - 1);
        if (!NameBanHandler.kickPlayer(playerName, player.getName(), kickReason)) {
            player.sendMessage(ChatColor.RED + "* Error: That player is not online.");
            return true;
        }
        StringBuilder kickMessageBuilder = new StringBuilder();
        kickMessageBuilder.append(ChatColor.AQUA);
        kickMessageBuilder.append("You've kicked ");
        kickMessageBuilder.append(ChatColor.DARK_GREEN);
        kickMessageBuilder.append(playerName);
        kickMessageBuilder.append(ChatColor.AQUA);
        kickMessageBuilder.append(" for ");
        kickMessageBuilder.append(ChatColor.DARK_GREEN);
        kickMessageBuilder.append(kickReason);
        kickMessageBuilder.append(ChatColor.AQUA);
        kickMessageBuilder.append(".");
        player.sendMessage(kickMessageBuilder.toString());
        return true;
    }
