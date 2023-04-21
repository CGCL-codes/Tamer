    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String arguments[]) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (arguments.length < 4) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !banip [IP-address] [interval] [h/d] [reason]");
            return;
        }
        int banInterval;
        char banIntervalCharacter;
        String ipAddress = arguments[0];
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
        if (!ipAddress.matches("(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)")) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error: Invalid ip-address format. Use: *.*.*.* (where * can be a number from 0 to 255, or a wildcard chracter).");
            return;
        }
        if (!Utilities.isNumeric(durationParameters[0])) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !banip [IP-address] [interval] [h/d] [reason]");
            return;
        }
        banInterval = Integer.parseInt(durationParameters[0]);
        banIntervalCharacter = durationParameters[1].charAt(0);
        if (banIntervalCharacter != 'h' && banIntervalCharacter != 'd') {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !banip [IP-address] [interval] [h/d] [reason]");
            return;
        }
        Date expireDate;
        if (banInterval != 0) {
            Date nowDate = new Date();
            expireDate = Utilities.addSeconds(nowDate, banInterval, banIntervalCharacter);
        } else {
            expireDate = new Date(0);
        }
        int returnValue = IPBanHandler.addBan(ipAddress, banReason, expireDate);
        if (returnValue == NameBanHandler.BAN_FAIL) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error:" + Colors.NORMAL + " An unknown error occured when trying to ban an ip-address.");
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
        banMessageBuilder.append("IP address ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(ipAddress);
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(" has been banned for ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(banReason);
        if (banInterval != 0) {
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append(" and no one using it will be able to connect until ");
            banMessageBuilder.append(Colors.DARK_GREEN);
            banMessageBuilder.append(dateFormatter.format(expireDate));
            banMessageBuilder.append(" (GMT + 1)");
        }
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(".");
        Main.getInstance().getIRCHandler().sendMessage(channel, banMessageBuilder.toString());
    }
