    public static boolean checkBan(String playerAddress, PlayerLoginEvent loginEvent, Player player) {
        try {
            long playerIp = Utilities.ipToLong(playerAddress);
            PreparedStatement queryStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT ban_id, start_range, end_range, reason, UNIX_TIMESTAMP(expire_date) FROM lvm_ip_bans WHERE (? >= start_range AND ? <= end_range) OR (? = start_range AND ? = end_range)");
            queryStatement.setLong(1, playerIp);
            queryStatement.setLong(2, playerIp);
            queryStatement.setLong(3, playerIp);
            queryStatement.setLong(4, playerIp);
            queryStatement.execute();
            ResultSet queryResult = queryStatement.getResultSet();
            if (!queryResult.next()) {
                return false;
            }
            boolean isPermanent = false;
            int banId = queryResult.getInt(1);
            long rangeStart = queryResult.getLong(2);
            long rangeEnd = queryResult.getLong(3);
            String banReason = queryResult.getString(4);
            Date expireDate = new Date(queryResult.getLong(5) * 1000);
            MinegroundPlayer playerInstance = Main.getInstance().getPlayer(player);
            if (!expireDate.after(new Date()) && queryResult.getLong(5) != 0L) {
                removeBan(banId);
                return false;
            }
            if (queryResult.getLong(5) == 0) {
                isPermanent = true;
            }
            if (playerInstance != null) {
                int profileId = playerInstance.getProfileId();
                queryStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT ban_id FROM lvm_ip_ban_exceptions WHERE ban_id = ? AND player_id = ?");
                queryStatement.setInt(1, banId);
                queryStatement.setInt(2, profileId);
                queryStatement.execute();
                queryResult = queryStatement.getResultSet();
                if (queryResult.next()) {
                    StringBuilder crewInformBuilder = new StringBuilder();
                    crewInformBuilder.append(Colors.RED);
                    crewInformBuilder.append("Notice: ");
                    crewInformBuilder.append(Colors.NORMAL);
                    crewInformBuilder.append("IP ");
                    crewInformBuilder.append(playerAddress);
                    crewInformBuilder.append(" (");
                    crewInformBuilder.append(player.getName());
                    crewInformBuilder.append(") matched with banned address [");
                    crewInformBuilder.append(banId);
                    crewInformBuilder.append("] ");
                    crewInformBuilder.append(getRangeAsString(rangeStart, rangeEnd));
                    crewInformBuilder.append(". Nick is on the exceptions list.");
                    Main.getInstance().getIRCHandler().sendMessage(Main.getInstance().getConfigHandler().ircDevChannel, crewInformBuilder.toString());
                    return false;
                }
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy (HH:mm)", Main.DEFAULT_LOCALE);
            StringBuilder kickMessageBuilder = new StringBuilder();
            if (!isPermanent) {
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("Your ip-address is banned until ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(dateFormatter.format(expireDate));
                kickMessageBuilder.append(" GMT + 1");
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Reason: ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(banReason);
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Appeal at www.mineground.com");
            } else {
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("Your ip-address is permanently banned");
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Reason: ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(banReason);
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Appeal at www.mineground.com");
            }
            loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());
            StringBuilder crewInformBuilder = new StringBuilder();
            crewInformBuilder.append(Colors.RED);
            crewInformBuilder.append("Notice: ");
            crewInformBuilder.append(Colors.NORMAL);
            crewInformBuilder.append("IP ");
            crewInformBuilder.append(playerAddress);
            crewInformBuilder.append(" (");
            crewInformBuilder.append(player.getName());
            crewInformBuilder.append(") matched with banned address [");
            crewInformBuilder.append(banId);
            crewInformBuilder.append("] ");
            crewInformBuilder.append(getRangeAsString(rangeStart, rangeEnd));
            crewInformBuilder.append(". Nick is not on the exceptions list. Banning ");
            crewInformBuilder.append(player.getName());
            crewInformBuilder.append("...");
            Main.getInstance().getIRCHandler().sendMessage(Main.getInstance().getConfigHandler().ircDevChannel, crewInformBuilder.toString());
            queryStatement.close();
            return true;
        } catch (Exception exception) {
            ExceptionLogger.error("Exception caught", exception);
        }
        return false;
    }
