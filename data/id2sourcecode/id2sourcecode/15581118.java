    public AsyncAgiConnectionHandler(MappingStrategy mappingStrategy, AsyncAgiEvent asyncAgiStartEvent) throws IllegalArgumentException {
        super(mappingStrategy);
        if (!asyncAgiStartEvent.isStart()) {
            throw new IllegalArgumentException("AsyncAgiEvent passed to AsyncAgiConnectionHandler is not a start sub event");
        }
        connection = (ManagerConnection) asyncAgiStartEvent.getSource();
        channelName = asyncAgiStartEvent.getChannel();
        environment = asyncAgiStartEvent.decodeEnv();
        asyncAgiEvents = new LinkedBlockingQueue<AsyncAgiEvent>();
        setIgnoreMissingScripts(true);
    }
