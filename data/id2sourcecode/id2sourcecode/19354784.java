    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String args[]) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (args.length < 1) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !why [exact player name] ([page number])");
            return;
        }
        int pageNumber = 1;
        int index;
        String playerName = args[0];
        StringBuilder outputMessageBuilder = new StringBuilder();
        Connection databaseConnection = Main.getInstance().getDatabaseHandler().getConnection();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm", Main.DEFAULT_LOCALE);
        if (args.length == 2 && Utilities.isNumeric(args[1])) {
            pageNumber = Integer.parseInt(args[1]);
        }
        try {
            PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT player_id FROM lvm_players WHERE login_name = ?");
            preparedStatement.setString(1, playerName);
            preparedStatement.execute();
            ResultSet queryResult = preparedStatement.getResultSet();
            if (!queryResult.next()) {
                Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error:" + Colors.NORMAL + " That player has never joined LVM.");
                return;
            }
            int profileId = queryResult.getInt(1);
            preparedStatement = databaseConnection.prepareStatement("SELECT player_id FROM lvm_player_log WHERE player_id = ?");
            preparedStatement.setInt(1, profileId);
            preparedStatement.execute();
            queryResult = preparedStatement.getResultSet();
            queryResult.last();
            index = queryResult.getRow();
            if (index == 0) {
                outputMessageBuilder.append(Colors.RED);
                outputMessageBuilder.append("*** Player log of ");
                outputMessageBuilder.append(playerName);
                outputMessageBuilder.append(" (0 items)");
                Main.getInstance().getIRCHandler().sendMessage(channel, outputMessageBuilder.toString());
                Main.getInstance().getIRCHandler().sendMessage(channel, "No items found.");
                return;
            }
            int rest = index % 5;
            int totalPages = (index - rest) / 5;
            totalPages = (rest == 0) ? (totalPages) : (totalPages + 1);
            if (pageNumber < 1 || pageNumber > totalPages) {
                Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error:" + Colors.NORMAL + " Invalid page number.");
                return;
            }
            preparedStatement = databaseConnection.prepareStatement("SELECT action_id, UNIX_TIMESTAMP(entry_date), admin, reason FROM lvm_player_log WHERE player_id = ? ORDER BY entry_id DESC LIMIT ?, 5");
            preparedStatement.setInt(1, profileId);
            preparedStatement.setInt(2, (5 * pageNumber) - 5);
            preparedStatement.execute();
            queryResult = preparedStatement.getResultSet();
            outputMessageBuilder.append(Colors.RED);
            outputMessageBuilder.append("*** Player log of ");
            outputMessageBuilder.append(playerName);
            outputMessageBuilder.append(" (");
            outputMessageBuilder.append(index);
            outputMessageBuilder.append(" items | Page ");
            outputMessageBuilder.append(pageNumber);
            outputMessageBuilder.append(" of ");
            outputMessageBuilder.append(totalPages);
            outputMessageBuilder.append(")");
            Main.getInstance().getIRCHandler().sendMessage(channel, outputMessageBuilder.toString());
            short actionId;
            long logDate;
            String adminName;
            String logReason;
            while (queryResult.next()) {
                actionId = queryResult.getShort(1);
                logDate = queryResult.getLong(2) * 1000;
                adminName = queryResult.getString(3);
                logReason = queryResult.getString(4);
                outputMessageBuilder = new StringBuilder();
                outputMessageBuilder.append(Colors.RED);
                outputMessageBuilder.append("[");
                outputMessageBuilder.append(dateFormatter.format(new Date(logDate)));
                outputMessageBuilder.append("]");
                outputMessageBuilder.append(Colors.DARK_GREEN);
                outputMessageBuilder.append(" (");
                outputMessageBuilder.append(PlayerLogManager.actionVerbs[actionId]);
                outputMessageBuilder.append(" by ");
                outputMessageBuilder.append(adminName);
                outputMessageBuilder.append(")");
                outputMessageBuilder.append(Colors.NORMAL);
                outputMessageBuilder.append(": ");
                outputMessageBuilder.append(logReason);
                Main.getInstance().getIRCHandler().sendMessage(channel, outputMessageBuilder.toString());
            }
            preparedStatement.close();
        } catch (Exception exception) {
            ExceptionLogger.error("Exception caught", exception);
        }
    }
