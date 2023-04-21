    public static boolean checkBan(PlayerLoginEvent loginEvent, MinegroundPlayer player) {
        try {
            PreparedStatement preparedStatement;
            if (!player.isRegistered()) {
                preparedStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT ban_id, reason, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_name = ?");
                preparedStatement.setString(1, player.getPlayer().getName());
            } else {
                preparedStatement = Main.getInstance().getDatabaseHandler().getConnection().prepareStatement("SELECT ban_id, reason, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_id = ?");
                preparedStatement.setInt(1, player.getProfileId());
            }
            preparedStatement.execute();
            ResultSet queryResult = preparedStatement.getResultSet();
            if (!queryResult.next()) {
                return false;
            }
            long sqlExpireDate = queryResult.getLong(3);
            int banId = queryResult.getInt(1);
            String reasonMessage = queryResult.getString(2);
            if (sqlExpireDate == 0) {
                StringBuilder kickMessageBuilder = new StringBuilder();
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("You are banned from this server / Reason: ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(reasonMessage);
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Appeal at www.mineground.com");
                loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());
                kickMessageBuilder = new StringBuilder();
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(Utilities.fixName(loginEvent.getPlayer()));
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(" attempted to join the server while being banned. (Reason: ");
                kickMessageBuilder.append(Colors.RED);
                kickMessageBuilder.append(reasonMessage);
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(" / Expire date: ");
                kickMessageBuilder.append(Colors.RED);
                kickMessageBuilder.append("Permanent");
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(")");
                Main.getInstance().getIRCHandler().sendMessage(Main.getInstance().getConfigHandler().ircDevChannel, kickMessageBuilder.toString());
                return true;
            }
            Date expireDate = new Date(sqlExpireDate * 1000);
            Date nowDate = new Date();
            if (expireDate.after(nowDate)) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy (HH:mm)", Main.DEFAULT_LOCALE);
                StringBuilder kickMessageBuilder = new StringBuilder();
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("You are banned until ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(dateFormatter.format(expireDate));
                kickMessageBuilder.append(" GMT + 1");
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Reason: ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(reasonMessage);
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append(" / Appeal at www.mineground.com");
                loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());
                preparedStatement.close();
                kickMessageBuilder = new StringBuilder();
                kickMessageBuilder.append(Colors.RED);
                kickMessageBuilder.append("Notice: ");
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(Utilities.fixName(loginEvent.getPlayer()));
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(" attempted to join the server while being banned.  (Reason: ");
                kickMessageBuilder.append(Colors.RED);
                kickMessageBuilder.append(reasonMessage);
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(" / Expire date: ");
                kickMessageBuilder.append(Colors.RED);
                kickMessageBuilder.append(dateFormatter.format(expireDate));
                kickMessageBuilder.append(Colors.BROWN);
                kickMessageBuilder.append(")");
                Main.getInstance().getIRCHandler().sendMessage(Main.getInstance().getConfigHandler().ircDevChannel, kickMessageBuilder.toString());
                return true;
            }
            removeBan(banId);
            preparedStatement.close();
        } catch (Exception exception) {
            ExceptionLogger.error("Exception caught", exception);
        }
        return false;
    }
