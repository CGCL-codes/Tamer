    @Inject
    public CatalogQueryRequestHandlerImpl(EventBus bus, UserCatalogListBuilder reader, UserCatalogListWriter writer, CatalogMetadataQueryServiceLocator childServiceLocator) {
        super(bus, new SimpleRequestHandlingProcess<List<String>>(reader, writer), childServiceLocator);
    }
