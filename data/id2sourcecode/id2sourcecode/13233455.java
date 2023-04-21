    public static void initXMPP() {
        try {
            String XMPPServer = PreferenceManager.getDefault().get("XMPP Server");
            ConnectionConfiguration config = new ConnectionConfiguration(XMPPServer);
            config.setDebuggerEnabled(true);
            connection = new XMPPConnection(XMPPServer);
            connection.connect();
            if (connection.isConnected()) {
                String login = PreferenceManager.getDefault().get("login");
                String password = PreferenceManager.getDefault().get("password");
                connection.login(login, password, java.net.InetAddress.getLocalHost().getHostName());
                if (connection.isAuthenticated()) {
                    chat = connection.getChatManager().createChat(PreferenceManager.getDefault().get("master"), ConnectionManager.getDefault().getChatListener());
                    ConnectionManager.getDefault().registerChat(chat);
                    Notifier.sendAlert("Connected", "AIMEE is now connected to " + chat.getParticipant());
                } else {
                    if (Options.get("showLoginFailure") != null) {
                        System.out.println("Failed to login using " + XMPPServer + "\n" + login + "\n" + password);
                    }
                    Notifier.sendAlert("Login Failure", "Could not log you in, try again later.");
                }
            } else {
                Notifier.sendAlert("Error!", "Could not connect to " + XMPPServer + "\nCheck your settings and try again");
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Notifier.sendException(ex);
        } catch (XMPPException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            Notifier.sendException(ex);
        }
    }
