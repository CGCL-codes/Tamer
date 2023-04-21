    private void isIPBanned(String ipAddress, String channel) {
        try {
            long rangeStart = IPBanHandler.getRangeStart(ipAddress);
            long rangeEnd = IPBanHandler.getRangeEnd(ipAddress);
            PreparedStatement preparedStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT reason, UNIX_TIMESTAMP(expire_date) FROM lvm_ip_bans WHERE ? >= start_range AND ? <= end_range");
            preparedStatement.setLong(1, rangeStart);
            preparedStatement.setLong(2, rangeEnd);
            preparedStatement.execute();
            ResultSet queryResult = preparedStatement.getResultSet();
            if (!queryResult.next()) {
                Main.getInstance().getIRCHandler().sendMessage(channel, Colors.DARK_GREEN + ipAddress + " is not banned.");
                return;
            }
            String reason = queryResult.getString(1);
            long expireDate = queryResult.getLong(2);
            StringBuilder messageBuilder = new StringBuilder();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm", Main.DEFAULT_LOCALE);
            messageBuilder.append(Colors.TEAL);
            messageBuilder.append("IP ");
            messageBuilder.append(Colors.DARK_GREEN);
            messageBuilder.append(ipAddress);
            messageBuilder.append(Colors.TEAL);
            messageBuilder.append(" was banned for ");
            messageBuilder.append(Colors.DARK_GREEN);
            messageBuilder.append(reason);
            if (expireDate > 0) {
                messageBuilder.append(Colors.TEAL);
                messageBuilder.append(" and no one using it will be able to connect until ");
                messageBuilder.append(Colors.DARK_GREEN);
                messageBuilder.append(dateFormatter.format(new Date(expireDate * 1000)));
            }
            messageBuilder.append(Colors.TEAL);
            messageBuilder.append(".");
            Main.getInstance().getIRCHandler().sendMessage(channel, messageBuilder.toString());
            preparedStatement.close();
        } catch (Exception exception) {
            ExceptionLogger.error("Exception caught", exception);
        }
    }
