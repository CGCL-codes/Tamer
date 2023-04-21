    public static boolean processInput(RedirectClient s, Session em) throws IOException {
        boolean protocolSplitVersion = false;
        if (s.version >= 9) protocolSplitVersion = true;
        if (s.state == CONNECTED) {
            s.log.setUsername(Server.readEncodedLine(s));
            String pass = "", plattform = "";
            s.log.setAppName(Server.readEncodedLine(s));
            s.log.setDeviceName(Server.readEncodedLine(s));
            s.log.setDeviceInstanceID(Server.readEncodedLine(s));
            if (protocolSplitVersion) s.log.setInstallDate(HelperStd.parseLong(Server.readEncodedLine(s), -1)); else s.log.setInstallDate(HelperStd.parseLong(Server.readLine(s), -1));
            if (protocolSplitVersion) s.log.setLanguage(Server.readEncodedLine(s)); else s.log.setLanguage(Server.readLine(s));
            s.locale = new Locale(s.log.getLanguage());
            s.log.setTel(Server.readEncodedLine(s));
            if (protocolSplitVersion) s.displayWidth = Integer.parseInt(Server.readEncodedLine(s)); else s.displayWidth = Integer.parseInt(Server.readLine(s));
            if (protocolSplitVersion) s.displayHeight = Integer.parseInt(Server.readEncodedLine(s)); else s.displayHeight = Integer.parseInt(Server.readLine(s));
            if (s.version >= 7) {
                plattform = Server.readEncodedLine(s);
                pass = Server.readEncodedLine(s);
                s.formatImg = Server.readEncodedLine(s);
                s.formatAudio = Server.readEncodedLine(s);
                s.formatVideo = Server.readEncodedLine(s);
                s.msgID = new String[] { s.log.getUsername(), pass, plattform };
            }
            if (s.version >= 2) {
                if (protocolSplitVersion) s.log.setAppVersion(Server.readEncodedLine(s)); else s.log.setAppVersion(Server.readLine(s));
                if (protocolSplitVersion) s.clientMemoryTotal = Long.parseLong(Server.readEncodedLine(s)); else s.clientMemoryTotal = Long.parseLong(Server.readLine(s));
                if (s.version >= 3) s.plugins = Server.readEncodedLine(s);
                if (s.version >= 10) {
                    int numValues = HelperStd.parseInt(Server.readEncodedLine(s), 0);
                    for (int i = 0; i < numValues; i++) {
                        String v = Server.readEncodedLine(s);
                        if (i == 0) s.fontMetrics = v; else if (i == 1) s.contentSize = v;
                    }
                }
            }
            if (Server.USE_DB_ADVERTISING) {
                s.werbungen.username = s.log.getUsername();
                s.werbungen.appVersion = s.log.getAppVersion();
                s.werbungen.appName = s.log.getAppName();
                s.werbungen.deviceName = s.log.getDeviceName();
                if (!Queues.offerElementToWerbungsQueue(s.werbungen)) {
                    Log.error("advertising queue is too small!!!");
                }
            }
            s.log.setTimeLoggedIn(new Date().getTime());
            if (checkIFUserIsBlocked(s)) {
                System.out.println("user is blocked!!!");
                doQuite(s);
                return true;
            }
            if (Server.USE_DB_LOGGING) Server.executor.saveDBObject(s.log);
            s.state = AUTHENTICATED;
            Werbung ad = null;
            if (s.version >= 16) ad = Helper.getStdWerbungImageAuth(s, true);
            if (ad == null) write(s, "ready"); else {
                writeProgressAd(s, ad);
            }
            String userDataId = s.log.getUsername() + pass + plattform + s.log.getDeviceInstanceID() + s.log.getAppName();
            try {
                s.userData = (UserData) em.get(UserData.class, userDataId);
                if (s.userData != null && s.userData.getHistory() != null && System.currentTimeMillis() - s.userData.getLastAccess() < 1200 * 1000) {
                    s.history = s.userData.getHistory();
                }
            } catch (org.hibernate.type.SerializationException se) {
                try {
                    em.createQuery("DELETE FROM UserData WHERE id=:id").setParameter("id", userDataId).executeUpdate();
                } catch (Exception ee) {
                }
            } catch (Exception e) {
            }
            if (s.userData == null) {
                s.userData = new UserData(userDataId);
                s.userData.isNew = true;
            }
        } else if (s.state == AUTHENTICATED) {
            String theInput = protocolSplitVersion ? Server.readEncodedLine(s) : Server.readLine(s);
            if (theInput.compareTo("0") == 0) {
                if (s.requestBuffer.isAvaliable()) theInput = protocolSplitVersion ? Server.readEncodedLine(s) : Server.readLine(s); else return false;
            } else if (theInput.compareTo("1") == 0) {
                write(s, "1");
                if (s.requestBuffer.isAvaliable()) theInput = protocolSplitVersion ? Server.readEncodedLine(s) : Server.readLine(s); else return false;
            }
            if (theInput.compareTo("exit") == 0) {
                doQuite(s);
                return true;
            } else if (theInput.compareTo("url") == 0) {
                processRedirect(s, false, em);
            } else if (theInput.compareTo("cache") == 0) {
                processRedirect(s, true, em);
            }
        }
        return false;
    }
