    public void connect(String server, String user, String password, boolean newUser) throws JabberMessageException, UnknownHostException, ConnectionFailedException, SendMessageFailedException, ParseException {
        if (this.session == null) {
            if (server == null || user == null || password == null) throw new NullPointerException();
            if (newUser) {
                Jabber jabber = new Jabber();
                JabberContext context = new JabberContext(null, null, server);
                this.session = jabber.createSession(context);
                this.session.connect(server, 5222);
                JabberUserService userService = this.session.getUserService();
                HashMap fieldMap = userService.getRegisterFields("jabber.org");
                Iterator iter = fieldMap.keySet().iterator();
                fieldMap.put("username", user);
                fieldMap.put("password", password);
                try {
                    userService.register("jabber.org", fieldMap);
                } catch (JabberMessageException e) {
                    JOptionPane.showMessageDialog(this, e.getErrorMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                    this.session.disconnect();
                    this.session = null;
                    return;
                }
                this.session.disconnect();
            }
            Jabber jabber = new Jabber();
            DefaultMessageParser parser = new DefaultMessageParser();
            parser.setParser("message", JabberCode.XMLNS_CHAT, "org.convey.JabberConveyMessage");
            JabberContext context = new JabberContext(user, password, server);
            this.session = jabber.createSession(context, parser);
            this.session.connect(server, 5222);
            this.session.getUserService().login();
            JabberPresenceMessage msg = new JabberPresenceMessage(PresenceCode.TYPE_AVAILABLE);
            msg.setPriority("1");
            msg.setShowState("Online");
            msg.setStatus("Online");
            String str = msg.encode();
            this.session.getPresenceService().setToAvailable("Online", null);
            final ConveyFrame thisConveyFrame = this;
            JabberMessageListener listener = new JabberMessageListener() {

                public void messageReceived(JabberMessageEvent evt) {
                    if (evt.getMessageType() == JabberCode.MSG_CHAT) {
                        JabberConveyMessage msg = (JabberConveyMessage) evt.getMessage();
                        thisConveyFrame.handleMessage(msg);
                    }
                }
            };
            this.session.addMessageListener(listener);
            this.from = user + "@" + server;
            this.setTitle();
            this.disconnectAction.setEnabled(true);
            this.chatAction.setEnabled(true);
            this.connectAction.setEnabled(false);
        }
    }
