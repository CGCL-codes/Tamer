    public MessageRouterRunner(MessageRouter<T> router, MessageIOReader<? extends T> reader, MessageIOWriter<T> writer) {
        AssertUtils.assertNonNullArg(reader);
        setRouter(router);
        _reader = reader;
        _writer = writer;
    }
