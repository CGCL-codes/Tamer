    public StreamProcessor(XmppServer xmppServer, XmppFactory xmppFactory, Reader reader, Writer writer) throws IOException, XmlPullParserException {
        this.xmppServer = xmppServer;
        this.xmppFactory = xmppFactory;
        if (reader instanceof BufferedReader) {
            this.reader = reader;
        } else {
            this.reader = new BufferedReader(reader);
        }
        if (writer instanceof BufferedWriter) {
            this.writer = writer;
        } else {
            this.writer = new BufferedWriter(writer);
        }
        AuthenticationController authenticationController = xmppFactory.createAuthenticationController();
        authQuery = new AuthQuery(this, authenticationController);
        authSasl = new AuthSasl(this, authenticationController);
        authenticationController.addListener(authQuery);
        authenticationController.addListener(authSasl);
        RosterController rosterController = xmppFactory.createRosterController();
        roster = new Roster(this, rosterController);
        MessageController messageController = xmppFactory.createMessageController();
        message = new Message(this, messageController);
        xmlPullParser = xmppFactory.createXmlPullParser(this.reader);
        xmlWriter = new XmlWriter(this.writer);
    }
