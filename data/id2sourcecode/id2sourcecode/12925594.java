    @Override
    public void sendMessage(ChatMessage pMessage) {
        logger.info("envoit d'un message");
        ChatDao dao = ServerDaoFactoryImpl.getInstance().getChatDao();
        if (dao != null) {
            dao.sendMessage(pMessage);
        } else {
            logger.error("dao null");
        }
        synchronizeMessage(pMessage, pMessage.getChannel());
    }
