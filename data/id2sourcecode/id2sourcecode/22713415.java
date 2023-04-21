    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String arguments[]) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (arguments.length < 4) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !ban [exact player name] [interval] [h/d] [reason]");
            return;
        }
        int banInterval;
        char banIntervalCharacter;
        String playerName = arguments[0];
        String banReason;
        String[] durationParameters = new String[2];
        durationParameters[0] = arguments[1];
        durationParameters[1] = arguments[2];
        StringBuilder banReasonBuilder = new StringBuilder();
        for (int index = 3; index < arguments.length; index++) {
            banReasonBuilder.append(arguments[index]);
            banReasonBuilder.append(" ");
        }
        banReason = banReasonBuilder.toString().substring(0, banReasonBuilder.toString().length() - 1);
        if (!Utilities.isNumeric(durationParameters[0])) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !ban [exact player name] [interval] [h/d] [reason]");
            return;
        }
        banInterval = Integer.parseInt(durationParameters[0]);
        banIntervalCharacter = durationParameters[1].charAt(0);
        if (banIntervalCharacter != 'h' && banIntervalCharacter != 'd') {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !ban [exact player name] [interval] [h/d] [reason]");
            return;
        }
        Date expireDate;
        if (banInterval != 0) {
            Date nowDate = new Date();
            expireDate = Utilities.addSeconds(nowDate, banInterval, banIntervalCharacter);
        } else {
            expireDate = new Date(0);
        }
        int returnValue = NameBanHandler.addBan(playerName, sender.getNick(), banReason, expireDate);
        if (returnValue == NameBanHandler.BAN_FAIL) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error:" + Colors.NORMAL + " That player has never joined LVM.");
            return;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm", Main.DEFAULT_LOCALE);
        StringBuilder banMessageBuilder = new StringBuilder();
        if (returnValue == NameBanHandler.BAN_ADD) {
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append("The existing ban has been extended by ");
            banMessageBuilder.append(Colors.DARK_GREEN);
            banMessageBuilder.append(banInterval);
            banMessageBuilder.append((banIntervalCharacter == 'h') ? (" hours") : (" days"));
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append(".");
            Main.getInstance().getIRCHandler().sendMessage(channel, banMessageBuilder.toString());
            return;
        }
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append("You've banned ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(playerName);
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(" for ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(banReason);
        if (banInterval != 0) {
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append(" and he/she won't be able to connect to the server until ");
            banMessageBuilder.append(Colors.DARK_GREEN);
            banMessageBuilder.append(dateFormatter.format(expireDate));
            banMessageBuilder.append(" (GMT + 1)");
        }
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(".");
        Main.getInstance().getIRCHandler().sendMessage(channel, banMessageBuilder.toString());
    }
