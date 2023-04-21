    private void isNameBanned(String playerName, String channel) {
        try {
            PreparedStatement preparedStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT player_id FROM lvm_players WHERE login_name = ?");
            preparedStatement.setString(1, playerName);
            preparedStatement.execute();
            ResultSet queryResult = preparedStatement.getResultSet();
            if (!queryResult.next()) {
                Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error: Invalid player name / ip-address.");
                return;
            }
            int profileId = queryResult.getInt(1);
            preparedStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT reason, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_id = ?");
            preparedStatement.setInt(1, profileId);
            preparedStatement.execute();
            queryResult = preparedStatement.getResultSet();
            if (!queryResult.next()) {
                Main.getInstance().getIRCHandler().sendMessage(channel, Colors.DARK_GREEN + playerName + " is not banned.");
                return;
            }
            String reason = queryResult.getString(1);
            long expireDate = queryResult.getLong(2);
            StringBuilder messageBuilder = new StringBuilder();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm", Main.DEFAULT_LOCALE);
            messageBuilder.append(Colors.DARK_GREEN);
            messageBuilder.append(playerName);
            messageBuilder.append(Colors.TEAL);
            messageBuilder.append(" was banned for ");
            messageBuilder.append(Colors.DARK_GREEN);
            messageBuilder.append(reason);
            if (expireDate > 0) {
                messageBuilder.append(Colors.TEAL);
                messageBuilder.append(" and won't be able to play until ");
                messageBuilder.append(Colors.DARK_GREEN);
                messageBuilder.append(dateFormatter.format(new Date(expireDate * 1000)));
            }
            messageBuilder.append(Colors.TEAL);
            messageBuilder.append(".");
            Main.getInstance().getIRCHandler().sendMessage(channel, messageBuilder.toString());
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.BROWN + "Use '!why " + playerName + "' for further information.");
            preparedStatement.close();
        } catch (Exception exception) {
            ExceptionLogger.error("Exception caught", exception);
        }
    }
