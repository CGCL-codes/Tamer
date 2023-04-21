        private boolean login() {
            final SessionManager sessionManager = ChatsyManager.getSessionManager();
            boolean hasErrors = false;
            String errorMessage = null;
            String serverName = "chat.mrswing.com";
            if (!hasErrors) {
                SmackConfiguration.setPacketReplyTimeout(10000);
                try {
                    ConnectionConfiguration config = new ConnectionConfiguration(serverName);
                    config.setReconnectionAllowed(true);
                    config.setRosterLoadedAtLogin(true);
                    config.setSendPresence(true);
                    connection = new XMPPConnection(config, this);
                    connection.connect();
                    connection.login(getUsername(), getPassword());
                    sessionManager.setServerAddress(connection.getServiceName());
                    sessionManager.initializeSession(connection, getUsername(), getPassword());
                    sessionManager.setJID(connection.getUser());
                } catch (XMPPException ex) {
                    final XMPPError error = ex.getXMPPError();
                    int errorCode = 0;
                    if (error != null) errorCode = error.getCode();
                    if (errorCode == 401) errorMessage = "Invalid username or password"; else if (errorCode == 502 || errorCode == 504) errorMessage = "Server unavailable"; else if (errorCode == 409) errorMessage = "Conflict error"; else errorMessage = "Can't connect to server";
                    Log.error(errorMessage, ex);
                    hasErrors = true;
                }
                if (hasErrors) {
                    final String finalErrorMessage = errorMessage;
                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            chatPref.putBoolean("loggedin", false);
                            loadingLabel.setVisible(false);
                            NotifyUtil.error("Login Error", finalErrorMessage, false);
                        }
                    });
                    return false;
                }
            }
            connection.addConnectionListener(ChatsyManager.getSessionManager());
            return !hasErrors;
        }
