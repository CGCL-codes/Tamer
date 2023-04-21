    public JabberListener(MindMapController c, MapSharingController sharingWizardController, String jabberServer, int port, String userName, String password) {
        controller = c;
        if (logger == null) {
            logger = controller.getController().getFrame().getLogger(this.getClass().getName());
        }
        commandQueue = new LinkedList();
        JabberContext context = new JabberContext(userName, password, jabberServer);
        Jabber jabber = new Jabber();
        session = jabber.createSession(context);
        try {
            session.connect(jabberServer, port);
            session.getUserService().login();
            logger.info("User logged in.\n");
            session.getPresenceService().setToAvailable("FreeMind Session", null, false);
            session.addMessageListener(new FreeMindJabberMessageListener(sharingWizardController));
        } catch (Exception ex) {
            freemind.main.Resources.getInstance().logException(ex);
            String message;
            if (ex.getClass().getName().compareTo("com.echomine.jabber.JabberMessageException") == 0) {
                JabberMessageException jabberMessageException = (JabberMessageException) ex;
                message = jabberMessageException.getErrorMessage();
            } else {
                message = ex.getClass().getName() + "\n\n" + ex.getMessage();
            }
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
