    @Override
    public void messageReceived(IoSession session, Object message) {
        Logger log = LoggerFactory.getLogger(ChatProtocolHandler.class);
        log.info("received: " + message);
        String theMessage = (String) message;
        String[] result = theMessage.split(" ", 2);
        String theCommand = result[0];
        try {
            ChatCommand command = ChatCommand.valueOf(theCommand);
            String user = (String) session.getAttribute("user");
            switch(command.toInt()) {
                case ChatCommand.QUIT:
                    session.write("QUIT OK");
                    session.close();
                    break;
                case ChatCommand.LOGIN:
                    if (user != null) {
                        session.write("LOGIN ERROR user " + user + " already logged in.");
                        return;
                    }
                    if (result.length == 2) {
                        user = result[1];
                    } else {
                        session.write("LOGIN ERROR invalid login command.");
                        return;
                    }
                    if (users.contains(user)) {
                        session.write("LOGIN ERROR the name " + user + " is already used.");
                        return;
                    }
                    sessions.add(session);
                    session.setAttribute("user", user);
                    MdcInjectionFilter.setProperty(session, "user", user);
                    users.add(user);
                    session.write("LOGIN OK");
                    broadcast("The user " + user + " has joined the chat session.");
                    break;
                case ChatCommand.BROADCAST:
                    if (result.length == 2) {
                        broadcast(user + ": " + result[1]);
                    }
                    break;
                default:
                    logger.info("Unhandled command: " + command);
                    break;
            }
        } catch (IllegalArgumentException e) {
            logger.debug("Illegal argument", e);
        }
    }
