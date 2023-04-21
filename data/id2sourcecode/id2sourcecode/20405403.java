    @Override
    public void onUserList(UserListEvent<CubeIRC> event) throws Exception {
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_USERLIST, event.getChannel());
        super.onUserList(event);
    }
