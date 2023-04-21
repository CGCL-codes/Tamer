    private void checkAuthent(MessageEvent e) {
        newSession = true;
        if (request.getMethod() == HttpMethod.GET) {
            String logon = Logon();
            responseContent.append(logon);
            clearSession();
            writeResponse(e.getChannel());
            return;
        } else if (request.getMethod() == HttpMethod.POST) {
            getParams();
            if (params == null) {
                String logon = Logon();
                responseContent.append(logon);
                clearSession();
                writeResponse(e.getChannel());
                return;
            }
        }
        boolean getMenu = false;
        if (params.containsKey("Logon")) {
            String name = null, password = null;
            List<String> values = null;
            if (!params.isEmpty()) {
                if (params.containsKey("name")) {
                    values = params.get("name");
                    if (values != null) {
                        name = values.get(0);
                        if (name == null || name.length() == 0) {
                            getMenu = true;
                        }
                    }
                } else {
                    getMenu = true;
                }
                if ((!getMenu) && params.containsKey("passwd")) {
                    values = params.get("passwd");
                    if (values != null) {
                        password = values.get(0);
                        if (password == null || password.length() == 0) {
                            getMenu = true;
                        } else {
                            getMenu = false;
                        }
                    } else {
                        getMenu = true;
                    }
                } else {
                    getMenu = true;
                }
            } else {
                getMenu = true;
            }
            if (!getMenu) {
                logger.debug("Name=" + name + " vs " + name.equals(FileBasedConfiguration.fileBasedConfiguration.ADMINNAME) + " Passwd=" + password + " vs " + FileBasedConfiguration.fileBasedConfiguration.checkPassword(password));
                if (name.equals(FileBasedConfiguration.fileBasedConfiguration.ADMINNAME) && FileBasedConfiguration.fileBasedConfiguration.checkPassword(password)) {
                    authentHttp.specialNoSessionAuth(FileBasedConfiguration.fileBasedConfiguration.HOST_ID);
                } else {
                    getMenu = true;
                }
                if (!authentHttp.isIdentified()) {
                    logger.debug("Still not authenticated: {}", authentHttp);
                    getMenu = true;
                }
                if (this.dbSession == null) {
                    try {
                        if (DbConstant.admin.isConnected) {
                            this.dbSession = new DbSession(DbConstant.admin, false);
                            DbAdmin.nbHttpSession++;
                            this.isPrivateDbSession = true;
                        }
                    } catch (GoldenGateDatabaseNoConnectionError e1) {
                        logger.warn("Use default database connection");
                        this.dbSession = DbConstant.admin.session;
                    }
                }
            }
        } else {
            getMenu = true;
        }
        if (getMenu) {
            String logon = Logon();
            responseContent.append(logon);
            clearSession();
            writeResponse(e.getChannel());
        } else {
            String index = index();
            responseContent.append(index);
            clearSession();
            admin = new DefaultCookie(FTPSESSION, Long.toHexString(new Random().nextLong()));
            sessions.put(admin.getValue(), this.authentHttp);
            if (this.isPrivateDbSession) {
                dbSessions.put(admin.getValue(), dbSession);
            }
            logger.debug("CreateSession: " + uriRequest + ":{}", admin);
            writeResponse(e.getChannel());
        }
    }
