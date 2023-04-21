    private void attemptLogin(IoSession session, char l, String username, String password) {
        try {
            if (TcpProtocolHandler.getPlayerCount() >= GameServer.getMaxPlayers()) {
                session.write("l2");
                return;
            }
            m_database = new MySqlManager();
            if (!m_database.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword())) {
                session.write("l1");
                return;
            }
            if (!m_database.selectDatabase(GameServer.getDatabaseName())) {
                session.write("l1");
                return;
            }
            ResultSet result = m_database.query("SELECT * FROM pn_bans WHERE ip='" + getIp(session) + "'");
            if (result != null && result.first()) {
                session.write("l4");
                return;
            }
            result = m_database.query("SELECT * FROM pn_members WHERE username='" + MySqlManager.parseSQL(username) + "'");
            if (!result.first()) {
                session.write("le");
                return;
            }
            if (result.getString("password").compareTo(password) == 0) {
                GameServer.getServiceManager().getMovementService().removePlayer(username);
                long time = System.currentTimeMillis();
                if (result.getString("lastLoginServer").equalsIgnoreCase(GameServer.getServerName())) {
                    if (TcpProtocolHandler.containsPlayer(username)) {
                        PlayerChar p = TcpProtocolHandler.getPlayer(username);
                        p.getTcpSession().setAttribute("player", null);
                        p.setLastLoginTime(time);
                        p.getTcpSession().close(true);
                        p.setTcpSession(session);
                        p.setLanguage(Language.values()[Integer.parseInt(String.valueOf(l))]);
                        m_database.query("UPDATE pn_members SET lastLoginServer='" + MySqlManager.parseSQL(GameServer.getServerName()) + "', lastLoginTime='" + time + "' WHERE username='" + MySqlManager.parseSQL(username) + "'");
                        m_database.query("UPDATE pn_members SET lastLoginIP='" + getIp(session) + "' WHERE username='" + MySqlManager.parseSQL(username) + "'");
                        m_database.query("UPDATE pn_members SET lastLanguageUsed='" + l + "' WHERE username='" + MySqlManager.parseSQL(username) + "'");
                        session.setAttribute("player", p);
                        this.initialiseClient(p, session);
                    } else {
                        session.write("l3");
                        return;
                    }
                } else if (result.getString("lastLoginServer").equalsIgnoreCase("null")) {
                    this.login(username, l, session, result);
                } else {
                    try {
                        Socket socket = new Socket(result.getString("lastLoginServer"), 7002);
                        socket.close();
                        session.write("l3");
                        return;
                    } catch (Exception e) {
                        this.login(username, l, session, result);
                    }
                }
            } else {
                session.write("le");
                return;
            }
            m_database.close();
        } catch (Exception e) {
            e.printStackTrace();
            session.write("lu");
            m_database.query("UPDATE pn_members SET lastLoginServer='null' WHERE username='" + MySqlManager.parseSQL(username) + "'");
            m_database.close();
        }
    }
