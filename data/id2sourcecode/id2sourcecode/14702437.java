    private void startGtalkSession() {
        if (gtalkLoginThread == null || !gtalkLoginThread.isAlive()) {
            gtalkLoginThread = new Thread() {

                @Override
                public void run() {
                    IDELoginDialog ld = new IDELoginDialog(ideInstance, "Enter your GTalk user name and password");
                    if (ld.showLoginDialog() == IDELoginDialog.IDEDialogState.OK) {
                        XMPPConfigReader xr = XMPPConfigReader.getInstance();
                        XMPPConfig xc = xr.getDefaultXMPPConfig();
                        String userName = ld.getUserName();
                        String domain = xc.getDomain();
                        if (userName.endsWith("@" + xc.getDomain())) {
                            userName = userName.split("@")[0];
                        } else {
                            if (userName.indexOf("@") > 0) {
                                domain = userName.split("@")[1];
                                userName = userName.split("@")[0];
                            }
                        }
                        ConnectionConfiguration connConfig = null;
                        System.out.println("Conneting to: " + domain);
                        if (domain.equals(xc.getDomain())) {
                            connConfig = new ConnectionConfiguration(xc.getServer(), xc.getPort(), xc.getDomain(), _proxyInfo);
                        } else {
                            connConfig = new ConnectionConfiguration(domain, xc.getPort(), _proxyInfo);
                        }
                        xmppConnection = new XMPPConnection(connConfig);
                        try {
                            System.out.println("Starting connection...");
                            xmppConnection.connect();
                            System.out.println("Connected.");
                            System.out.println("Logging in...");
                            xmppConnection.login(userName + "@" + domain, ld.getPasswordText());
                            System.out.println("Logged in.");
                            Presence presence = new Presence(Presence.Type.unavailable);
                            xmppConnection.sendPacket(presence);
                            System.out.println("Set the status");
                            Roster roster = xmppConnection.getRoster();
                            FloatingListModel model = (FloatingListModel) talkUserList.getModel();
                            for (RosterEntry re : roster.getEntries()) {
                                IDETalkUser tUser = new IDETalkUser(re.getName(), xc.getDomain(), re.getType().name(), IDETalkUser.TalkUserDomain.GTalk);
                                if (re.getName() == null) {
                                    tUser.setUserName(re.getUser());
                                }
                                tUser.setAdditionalInfo(re);
                                model.addElement(tUser);
                            }
                            startRemoteChatListener();
                            statusInvisible.setEnabled(true);
                            statusOnline.setEnabled(true);
                            signInSignOut.setText("Sign Out");
                        } catch (Exception err) {
                            JOptionPane.showMessageDialog(ideInstance, "Unable to login: " + userName + "@" + domain + ". Check your user name and" + " password and try again.");
                            System.err.println("Error while logging in: " + err);
                            err.printStackTrace();
                        }
                    }
                    ld.clearFields();
                    ld = null;
                }
            };
            gtalkLoginThread.setName("WorkflowBar gtalk login Thread");
            gtalkLoginThread.setPriority(Thread.MIN_PRIORITY);
            gtalkLoginThread.start();
        }
    }
