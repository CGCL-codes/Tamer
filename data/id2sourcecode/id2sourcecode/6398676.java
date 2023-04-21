    public ClientSessionListener loggedIn(ClientSession session) {
        DataManager dataManager = AppContext.getDataManager();
        logger.log(Level.INFO, "User {0} has logged in", session.getName());
        String playerBinding = "Player." + session.getName();
        String agentBinding = "Agent." + session.getName();
        UserSession userSession;
        GameAgent agent;
        try {
            userSession = dataManager.getBinding(playerBinding, UserSession.class);
            userSession.setSession(session);
            agent = dataManager.getBinding(agentBinding, GameAgent.class);
            logger.log(Level.INFO, "LOADED " + playerBinding + " " + agentBinding);
        } catch (NameNotBoundException exception) {
            logger.log(Level.INFO, exception.getMessage());
            userSession = new UserSession(session);
            dataManager.setBinding(playerBinding, userSession);
            agent = new GameAgent();
            agent.setPosition(new Position(100, 100, 0, 0));
            dataManager.setBinding(agentBinding, agent);
            logger.log(Level.INFO, "CREATED " + playerBinding + " " + agentBinding);
        }
        getChannel("chat").join(session, null);
        getChannel("position").join(session, null);
        MovementMessage message = new MovementMessage(agent.getId(), agent, agent.getSpeed());
        userSession.send(message);
        logger.log(Level.INFO, "done");
        return userSession;
    }
