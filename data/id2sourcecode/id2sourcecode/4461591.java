    public void connectToServer(final String state, final String type, final String mode) {
        SmackConfiguration.setPacketReplyTimeout(15000);
        String username = prefs.getString("prefJabberIdKey", null);
        final String password = prefs.getString("prefPasswordKey", null);
        int port = Integer.parseInt(prefs.getString("prefServerPortKey", "5222"));
        String service = (prefs.getString("prefServerKey", "")).trim();
        String host;
        final String user;
        if (username == null) {
            return;
        } else {
            host = StringUtils.parseServer(username);
            user = username.substring(0, username.indexOf("@"));
        }
        if (username.length() == 0) {
            return;
        }
        if (host.length() == 0) {
            return;
        }
        if (service == "") {
            cc = new ConnectionConfiguration(host, port);
        } else {
            cc = new ConnectionConfiguration(service, port, host);
        }
        cc.setTruststorePath(CERT_DIR + CERT_FILE);
        if (!prefs.getBoolean("prefEnableTlsKey", true)) {
            cc.setSecurityMode(SecurityMode.disabled);
        }
        if (!prefs.getBoolean("prefEnableSaslKey", true)) {
            cc.setSASLAuthenticationEnabled(false);
        }
        cc.setReconnectionAllowed(true);
        con = new XMPPConnection(cc);
        Thread task = new Thread() {

            public void run() {
                try {
                    con.connect();
                } catch (Exception xe) {
                    Log.e(TAG, "Could not connect to server: " + xe.getLocalizedMessage());
                    Intent intent = new Intent("cz.jabbim.android.androidim.CONNECTION_FAILED");
                    sendBroadcast(intent);
                    return;
                }
                try {
                    con.addPacketListener(msgListener, new PacketTypeFilter(Message.class));
                    con.addPacketListener(presenceListener, new PacketTypeFilter(Presence.class));
                    con.addConnectionListener(connectionListener);
                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    return;
                }
                try {
                    con.login(user, password, prefs.getString("prefResourceKey", "Android"));
                } catch (XMPPException xe) {
                    Log.e(TAG, "Could not login with given username(" + user + ") or password(" + password + "): " + xe.getLocalizedMessage());
                    return;
                }
                try {
                    roster = con.getRoster();
                    roster.addRosterListener(new RosterListener() {

                        public void entriesAdded(Collection<String> addresses) {
                        }

                        public void entriesDeleted(Collection<String> addresses) {
                        }

                        public void entriesUpdated(Collection<String> addresses) {
                        }

                        public void presenceChanged(Presence presence) {
                        }
                    });
                    sendRoster();
                    if (prefs.getBoolean("prefWakeLockKey", true)) {
                        wl.acquire();
                        Log.i(TAG, "Prevents from CPU suspend");
                    }
                    OfflineMessageManager omm = new OfflineMessageManager(con);
                    try {
                        omm.deleteMessages();
                    } catch (XMPPException e) {
                        Log.e(TAG, "Could not delete offline messages.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("cz.jabbim.android.androidim.PRESENCE_CHANGED");
                sendBroadcast(intent);
                Intent loggedIn = new Intent("cz.jabbim.android.androidim.LOGGED_IN");
                sendBroadcast(loggedIn);
            }
        };
        task.start();
    }
