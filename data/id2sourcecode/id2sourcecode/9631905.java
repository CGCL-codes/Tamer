    protected MessageRouterRunner<T> newMessageRouterRunner(MessageRouter<T> router, MessageIOReader<T> reader, MessageIOWriter<T> writer) {
        return new MessageRouterRunner<T>(router, reader, writer);
    }
