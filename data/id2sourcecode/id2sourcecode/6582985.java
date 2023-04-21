    @Override
    public void run() {
        logger.debug(langRes.getString("CLIENTCONN_STARTING"));
        new ClientConnectionListener(clientSocket, props).start();
        props.waitTillConnected();
        logger.debug(langRes.getString("CLIENTCONN_USER_AND_NICK_SENT"));
        logger.debug(langRes.getString("CLIENTCONN_SENDING_REPLIES"));
        try {
            sendMessage(IRCMessage.create(props.getServerHost(), "001", new String[] { props.getNickname(), langRes.getString("CLIENTCONN_WELCOME_TO_BOUNCER") + " " + props.getNickname() + "!" + props.getUsername() + "@" + props.getClientHost() }));
            sendMessage(IRCMessage.create(props.getServerHost(), "002", new String[] { props.getNickname(), langRes.getString("CLIENTCONN_YOUR_HOST_IS") + " " + props.getServerHost() + ", " + langRes.getString("CLIENTCONN_RUNNING_VERSION") + " " + Constants.VERSION }));
            sendMessage(IRCMessage.create(props.getServerHost(), "003", new String[] { props.getNickname(), langRes.getString("CLIENTCONN_CREATED") }));
            sendMessage(IRCMessage.create(props.getServerHost(), "004", new String[] { props.getNickname(), "gBouncer " + Constants.VERSION }));
            logger.debug(langRes.getString("CLIENTCONN_SENDING_MOTD"));
            sendMessage(IRCMessage.create(props.getServerHost(), "375", new String[] { props.getNickname(), "- " + props.getServerHost() + " Message of the day - " }));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fis = new FileInputStream("motd.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                sendMessage(IRCMessage.create(props.getServerHost(), "372", new String[] { props.getNickname(), "- " + line }));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            logger.warn(langRes.getString("CLIENTCONN_MOTD_NOT_FOUND"));
            try {
                sendMessage(IRCMessage.create(props.getServerHost(), "372", new String[] { props.getNickname(), "- " + langRes.getString("CLIENTCONN_MOTD_NOT_FOUND") }));
            } catch (RemoteException ex) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sendMessage(IRCMessage.create(props.getServerHost(), "376", new String[] { props.getNickname(), "End of /MOTD command" }));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            IServerConnection serverConn = connMgr.getServerConnenctionFromUser(usrMgr.getUser(props.getUsername()));
            if (serverConn != null) {
                IRCMessage[] unreadMessages = serverConn.getUnreadMessages();
                if (unreadMessages.length > 0) {
                    logger.debug(langRes.getString("CLIENTCONN_SENDING_UNREAD"));
                    sendMessage(IRCMessage.create(props.getServerHost(), "PRIVMSG", new String[] { props.getNickname(), langRes.getString("CLIENTCONN_UNREAD_MESSAGES") + ":" }));
                    for (IRCMessage message : unreadMessages) {
                        sendMessage(IRCMessage.create(props.getServerHost(), "PRIVMSG", new String[] { props.getNickname(), message.prefix + " -> " + message.params[1] }));
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ConnectionManagerException e) {
            e.printStackTrace();
        } catch (UserManagementException e) {
            e.printStackTrace();
        }
        try {
            IServerConnection serverConn = connMgr.getServerConnenctionFromUser(usrMgr.getUser(props.getUsername()));
            if (serverConn != null) {
                for (String channel : serverConn.getChannels()) {
                    String modifiedChannel = channel;
                    logger.debug(langRes.getString("CLIENTCONN_REJOIN_CHANNEL") + " (" + modifiedChannel + ")");
                    sendMessage(IRCMessage.create(props.getNickname() + "!" + props.getUsername() + "@" + props.getClientHost(), "JOIN", new String[] { modifiedChannel }));
                    serverConn.sendMessage(IRCMessage.create("", "TOPIC", new String[] { channel }));
                    serverConn.sendMessage(IRCMessage.create("", "NAMES", new String[] { channel }));
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ConnectionManagerException e) {
            e.printStackTrace();
        } catch (UserManagementException e) {
            e.printStackTrace();
        }
        new PingPongThread().start();
    }
