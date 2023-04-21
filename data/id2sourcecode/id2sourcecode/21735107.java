    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String command, String[] arguments) {
        Player player = (Player) cs;
        if (!player.hasPermission(PermissionNodes.PERMISSION_BAN_COMMAND)) {
            return true;
        }
        if (arguments.length < 4) {
            player.sendMessage(ChatColor.RED + "* Usage: '/ban <exact player name> <interval> <hour/day> <reason>'");
            return true;
        }
        int banInterval;
        char banIntervalCharacter;
        String playerName = arguments[0];
        String[] durationParameters = new String[2];
        durationParameters[0] = arguments[1];
        durationParameters[1] = arguments[2];
        String banReason;
        StringBuilder banReasonBuilder = new StringBuilder();
        for (int index = 3; index < arguments.length; index++) {
            banReasonBuilder.append(arguments[index]);
            banReasonBuilder.append(" ");
        }
        banReason = banReasonBuilder.toString().substring(0, banReasonBuilder.toString().length() - 1);
        if (!Utilities.isNumeric(durationParameters[0])) {
            player.sendMessage(ChatColor.RED + "* Usage: '/ban <exact player name> <interval> <hour/day> <reason>'");
            return true;
        }
        banInterval = Integer.parseInt(durationParameters[0]);
        banIntervalCharacter = durationParameters[1].charAt(0);
        if (banIntervalCharacter != 'h' && banIntervalCharacter != 'd') {
            player.sendMessage(ChatColor.RED + "* Usage: '/ban <exact player name> <interval> <hour/day> <reason>'");
            return true;
        }
        Date expireDate;
        if (banInterval != 0) {
            Date nowDate = new Date();
            expireDate = Utilities.addSeconds(nowDate, banInterval, banIntervalCharacter);
        } else {
            expireDate = new Date(0);
        }
        int returnValue = NameBanHandler.addBan(playerName, player.getName(), banReason, expireDate);
        if (returnValue == NameBanHandler.BAN_FAIL) {
            player.sendMessage(ChatColor.RED + "* Error: That player has never joined LVM.");
            return true;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm", Main.DEFAULT_LOCALE);
        StringBuilder banMessageBuilder = new StringBuilder();
        if (returnValue == NameBanHandler.BAN_ADD) {
            banMessageBuilder.append(ChatColor.AQUA);
            banMessageBuilder.append("The existing ban has been extended by ");
            banMessageBuilder.append(ChatColor.DARK_GREEN);
            banMessageBuilder.append(Utilities.formatTime(Math.round(expireDate.getTime() / 1000)));
            banMessageBuilder.append(ChatColor.AQUA);
            banMessageBuilder.append(".");
            player.sendMessage(banMessageBuilder.toString());
            return true;
        }
        banMessageBuilder.append(ChatColor.AQUA);
        banMessageBuilder.append("You've banned ");
        banMessageBuilder.append(ChatColor.DARK_GREEN);
        banMessageBuilder.append(playerName);
        banMessageBuilder.append(ChatColor.AQUA);
        banMessageBuilder.append(" for ");
        banMessageBuilder.append(ChatColor.DARK_GREEN);
        banMessageBuilder.append(banReason);
        if (banInterval != 0) {
            banMessageBuilder.append(ChatColor.AQUA);
            banMessageBuilder.append(" and he/she won't be able to connect to the server until ");
            banMessageBuilder.append(ChatColor.DARK_GREEN);
            banMessageBuilder.append(dateFormatter.format(expireDate));
            banMessageBuilder.append(" (GMT + 1)");
        }
        banMessageBuilder.append(ChatColor.AQUA);
        banMessageBuilder.append(".");
        player.sendMessage(banMessageBuilder.toString());
        return true;
    }
