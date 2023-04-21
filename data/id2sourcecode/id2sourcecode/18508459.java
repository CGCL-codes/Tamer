    jjsched(String config, boolean autocreate) {
        theMacros = new Macros();
        theConfig = new Config(theMacros);
        try {
            theConfig.parse(config);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            bad = true;
            return;
        }
        String server = null;
        String username = null;
        String password = null;
        try {
            server = theConfig.getString("XMPPServerName");
            username = theConfig.getString("XMPPUserName");
            password = theConfig.getString("XMPPPassword");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            bad = true;
            return;
        }
        dbc = new DatabaseConnector(theConfig, theTime);
        try {
            dbc.connect(autocreate);
        } catch (Exception e) {
            System.out.println("Error during MySQL initialization:");
            System.out.println(e.getMessage());
            bad = true;
            return;
        }
        theConnectionConfig = new ConnectionConfiguration(server, 5222);
        theConnectionConfig.setCompressionEnabled(true);
        theConnectionConfig.setSASLAuthenticationEnabled(true);
        try {
            theConnector = new Connector(new XMPPConnection(theConnectionConfig), dbc);
            theConnector.connect();
            theConnector.login(username, password);
        } catch (Exception e) {
            try {
                theConnector.disconnect();
            } catch (Exception e2) {
            }
            System.out.println("Could not connect to the XMPP server. Check your configuration file: " + e.getMessage());
            bad = true;
            return;
        }
        theParser = new CommandParser(dbc, theConfig, theMacros, theTime);
        theExecutor = new ActionExecutor(theConnector, dbc, theConfig, theTime, this);
        theConsole = new Console(theParser, theExecutor, theMacros);
        theConsole.start();
        theHandler = new PacketHandler(theConnector, theConfig, dbc, theTime, theMacros, theParser, theExecutor);
        theConnector.addMessageListener(theHandler);
        theSender = new Sender(dbc, theConnector);
    }
