    public boolean loginValid(String user, String password, L2LoginClient client) {
        boolean ok = false;
        InetAddress address = client.getConnection().getInetAddress();
        if (address == null || user == null) {
            return false;
        }
        Connection con = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes("UTF-8");
            byte[] hash = md.digest(raw);
            byte[] expected = null;
            int access = 0;
            int lastServer = 1;
            String userIP = null;
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT password, accessLevel, lastServer, userIP FROM accounts WHERE login=?");
            statement.setString(1, user);
            ResultSet rset = statement.executeQuery();
            if (rset.next()) {
                expected = Base64.decode(rset.getString("password"));
                access = rset.getInt("accessLevel");
                lastServer = rset.getInt("lastServer");
                userIP = rset.getString("userIP");
                if (lastServer <= 0) lastServer = 1;
                if (Config.DEBUG) _log.fine("account exists");
            }
            rset.close();
            statement.close();
            if (expected == null) {
                if (Config.AUTO_CREATE_ACCOUNTS) {
                    if ((user.length() >= 2) && (user.length() <= 14)) {
                        statement = con.prepareStatement("INSERT INTO accounts (login,password,lastactive,accessLevel,lastIP) values(?,?,?,?,?)");
                        statement.setString(1, user);
                        statement.setString(2, Base64.encodeBytes(hash));
                        statement.setLong(3, System.currentTimeMillis());
                        statement.setInt(4, 0);
                        statement.setString(5, address.getHostAddress());
                        statement.execute();
                        statement.close();
                        if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - OK : AccountCreate", "loginlog");
                        _log.info("Created new account for " + user);
                        return true;
                    }
                    if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - ERR : ErrCreatingACC", "loginlog");
                    _log.warning("Invalid username creation/use attempt: " + user);
                    return false;
                } else {
                    if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - ERR : AccountMissing", "loginlog");
                    _log.warning("Account missing for user " + user);
                    FailedLoginAttempt failedAttempt = _hackProtection.get(address);
                    int failedCount;
                    if (failedAttempt == null) {
                        _hackProtection.put(address, new FailedLoginAttempt(address, password));
                        failedCount = 1;
                    } else {
                        failedAttempt.increaseCounter();
                        failedCount = failedAttempt.getCount();
                    }
                    if (failedCount >= Config.LOGIN_TRY_BEFORE_BAN) {
                        _log.info("Banning '" + address.getHostAddress() + "' for " + Config.LOGIN_BLOCK_AFTER_BAN + " seconds due to " + failedCount + " invalid user name attempts");
                        this.addBanForAddress(address, Config.LOGIN_BLOCK_AFTER_BAN * 1000);
                    }
                    return false;
                }
            } else {
                if (access < 0) {
                    if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - ERR : AccountBanned", "loginlog");
                    client.setAccessLevel(access);
                    return false;
                }
                if (userIP != null && !address.getHostAddress().equalsIgnoreCase(userIP)) {
                    if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + "/" + userIP + " - ERR : INCORRECT IP", "loginlog");
                    return false;
                }
                ok = true;
                for (int i = 0; i < expected.length; i++) {
                    if (hash[i] != expected[i]) {
                        ok = false;
                        break;
                    }
                }
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
            _log.log(Level.WARNING, "Could not check password:" + e.getMessage(), e);
            ok = false;
        } finally {
            L2DatabaseFactory.close(con);
        }
        if (!ok) {
            if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - ERR : LoginFailed", "loginlog");
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
            if (Config.LOG_LOGIN_CONTROLLER) Log.add("'" + user + "' " + address.getHostAddress() + " - OK : LoginOk", "loginlog");
        }
        return ok;
    }
