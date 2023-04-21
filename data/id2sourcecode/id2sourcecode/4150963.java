    protected Channel createChannel(String name, ChannelListener listener, SgsTestNode node) throws Exception {
        CreateChannelTask createChannelTask = new CreateChannelTask(name, listener);
        runTransactionalTask(createChannelTask, node);
        return createChannelTask.getChannel();
    }
