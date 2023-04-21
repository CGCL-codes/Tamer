    public LocalChannel(LocalEngine engine, String id) {
        super(id);
        this.engine = engine;
        this.metadata = engine.getWorkspace().getMetadata();
        this.location = new File(engine.getWorkspace().getChannelsDirectory(), getId());
    }
