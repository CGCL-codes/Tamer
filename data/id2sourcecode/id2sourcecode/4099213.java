        private boolean login() {
            final SessionManager sessionManager = SparkManager.getSessionManager();
            boolean hasErrors = false;
            String errorMessage = null;
            String serverName = getServerName();
            if (!hasErrors) {
                localPref = SettingsManager.getLocalPreferences();
                if (localPref.isDebuggerEnabled()) {
                    XMPPConnection.DEBUG_ENABLED = true;
                }
                SmackConfiguration.setPacketReplyTimeout(localPref.getTimeOut() * 1000);
                try {
                    int port = localPref.getXmppPort();
                    int checkForPort = serverName.indexOf(":");
                    if (checkForPort != -1) {
                        String portString = serverName.substring(checkForPort + 1);
                        if (ModelUtil.hasLength(portString)) {
                            port = Integer.valueOf(portString);
                        }
                    }
                    boolean useSSL = localPref.isSSL();
                    boolean hostPortConfigured = localPref.isHostAndPortConfigured();
                    ConnectionConfiguration config;
                    if (useSSL) {
                        if (!hostPortConfigured) {
                            config = new ConnectionConfiguration(serverName, 5223);
                            config.setSocketFactory(new DummySSLSocketFactory());
                        } else {
                            config = new ConnectionConfiguration(localPref.getXmppHost(), port, serverName);
                            config.setSocketFactory(new DummySSLSocketFactory());
                        }
                    } else {
                        if (!hostPortConfigured) {
                            config = new ConnectionConfiguration(serverName);
                        } else {
                            config = new ConnectionConfiguration(localPref.getXmppHost(), port, serverName);
                        }
                    }
                    config.setReconnectionAllowed(true);
                    config.setRosterLoadedAtLogin(true);
                    config.setSendPresence(false);
                    if (localPref.isPKIEnabled()) {
                        SASLAuthentication.supportSASLMechanism("EXTERNAL");
                        config.setKeystoreType(localPref.getPKIStore());
                        if (localPref.getPKIStore().equals("PKCS11")) {
                            config.setPKCS11Library(localPref.getPKCS11Library());
                        } else if (localPref.getPKIStore().equals("JKS")) {
                            config.setKeystoreType("JKS");
                            config.setKeystorePath(localPref.getJKSPath());
                        } else if (localPref.getPKIStore().equals("X509")) {
                        } else if (localPref.getPKIStore().equals("Apple")) {
                            config.setKeystoreType("Apple");
                        }
                    }
                    if (config != null) {
                        boolean compressionEnabled = localPref.isCompressionEnabled();
                        config.setCompressionEnabled(compressionEnabled);
                        connection = new XMPPConnection(config, this);
                        if (ModelUtil.hasLength(localPref.getTrustStorePath())) {
                            config.setTruststorePath(localPref.getTrustStorePath());
                            config.setTruststorePassword(localPref.getTrustStorePassword());
                        }
                    }
                    connection.connect();
                    String resource = localPref.getResource();
                    if (!ModelUtil.hasLength(resource)) {
                        resource = "TalkBG";
                    }
                    connection.login(getUsername(), getPassword(), resource);
                    sessionManager.setServerAddress(connection.getServiceName());
                    sessionManager.initializeSession(connection, getUsername(), getPassword());
                    sessionManager.setJID(connection.getUser());
                } catch (Exception xee) {
                    if (!loginDialog.isVisible()) {
                        loginDialog.setVisible(true);
                    }
                    if (xee instanceof XMPPException) {
                        XMPPException xe = (XMPPException) xee;
                        final XMPPError error = xe.getXMPPError();
                        int errorCode = 0;
                        if (error != null) {
                            errorCode = error.getCode();
                        }
                        if (errorCode == 401) {
                            errorMessage = Res.getString("message.invalid.username.password");
                        } else if (errorCode == 502 || errorCode == 504) {
                            errorMessage = Res.getString("message.server.unavailable");
                        } else if (errorCode == 409) {
                            errorMessage = Res.getString("label.conflict.error");
                        } else {
                            errorMessage = Res.getString("message.unrecoverable.error");
                        }
                    } else {
                        errorMessage = SparkRes.getString(SparkRes.UNRECOVERABLE_ERROR);
                    }
                    Log.warning("Exception in Login:", xee);
                    hasErrors = true;
                }
            }
            if (hasErrors) {
                progressBar.setVisible(false);
                if (loginDialog.isVisible()) {
                    if (!localPref.isSSOEnabled()) {
                        JOptionPane.showMessageDialog(loginDialog, errorMessage, Res.getString("title.login.error"), JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(loginDialog, "Unable to connect using Single Sign-On. Please check your principal and server settings.", Res.getString("title.login.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
                setEnabled(true);
                return false;
            }
            connection.addConnectionListener(SparkManager.getSessionManager());
            localPref.setUsername(getUsername());
            if (savePasswordBox.isSelected()) {
                String encodedPassword;
                try {
                    encodedPassword = Encryptor.encrypt(getPassword());
                    localPref.setPassword(encodedPassword);
                } catch (Exception e) {
                    Log.error("Error encrypting password.", e);
                }
            } else {
                localPref.setPassword("");
            }
            localPref.setSavePassword(savePasswordBox.isSelected());
            localPref.setAutoLogin(autoLoginBox.isSelected());
            localPref.setServer(serverField.getText());
            SettingsManager.saveSettings();
            return !hasErrors;
        }
