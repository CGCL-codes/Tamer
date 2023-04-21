    @Override
    public void connect(final String username, final String password) throws BotException {
        try {
            connection.connect();
            connection.login(username, password, resource);
        } catch (final XMPPException e) {
            throw new BotException("Bot connection to service fail " + e.getMessage());
        } catch (final NullPointerException e) {
            throw new BotException("Jid and password should not be empty.");
        }
        final ChatManager chatManager = connection.getChatManager();
        chatManager.addChatListener(new NotificationBotChatListener(service, delay));
    }
